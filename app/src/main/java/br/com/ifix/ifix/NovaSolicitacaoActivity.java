package br.com.ifix.ifix;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import api.HttpGlobalRetrofit;
import api.Response.Department;
import api.Response.Equipment;
import api.Response.Sector;
import api.deserializers.DepartmentDes;
import api.deserializers.EquipmentDes;
import api.deserializers.SectorDes;
import api.interfaces.DepartmentInterface;
import api.interfaces.EquipmentInterface;
import api.interfaces.SectorInterface;
import api.requests.RequestReq;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovaSolicitacaoActivity extends AppCompatActivity {

    private Spinner deparments_spinners;
    private Spinner sector_spinners;
    private Spinner equipments_spinner;
    private EditText subject_matter;
    private EditText description;
    private List<Department> departments;
    private List<Sector> sectors;
    private List<Equipment> equipments;

    ProgressDialog dialog;

    private int department_id;
    private int sector_id;
    private int equipment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_solicitacao);
        setTitle("Nova Solicitação");

        this.deparments_spinners = (Spinner) findViewById(R.id.deparments_spinner);
            this.deparments_spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    department_id = departments.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        this.sector_spinners = (Spinner) findViewById(R.id.sector_spinner);

        this.equipments_spinner = (Spinner) findViewById(R.id.equipments_spinner);

        this.sector_spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchEquipmentsBySector(sectors.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.subject_matter = (EditText) findViewById(R.id.request_subject_matter);
        this.description = (EditText) findViewById(R.id.request_description);

        TextView button_save = (TextView) findViewById(R.id.request_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRequest();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        searchDepartment();
        searchSector();
    }

    private void createRequest() {

        //Resetar Erros
        this.subject_matter.setError(null);
        this.description.setError(null);

        //Armazena os valores
        int department_id = getDepartmentSelect();
        int equipment_id = getEquipmentSelect();
        String subject_matter = this.subject_matter.getText().toString();
        String description = this.description.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Verifica se o campo subject_matter está vazio ou  menos de 3 caracteres
        if(!TextUtils.isEmpty(subject_matter) && subject_matter.length() < 3) {
            this.subject_matter.setError(getString(R.string.error_field_required));
            focusView = this.subject_matter;
            cancel = true;
        }

        //Verifica o campo de description está vazio ou tem menos que 3 caracteres
        if(!TextUtils.isEmpty(description) && description.length() < 3) {
            focusView = this.description;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        } else {
            RequestReq request = new RequestReq();
            request.setDepartment_id(department_id);
            request.setEquipment_id(equipment_id);
            request.setSubject_matter(subject_matter);
            request.setDescription(description);
            this.addRequest(request);
        }
    }

    private void addRequest(RequestReq req){
        Log.d("SOLICITA>>>>>>>>>>>>>>>", String.valueOf(req.getDepartment_id()));
        Log.d("SOLICITA>>>>>>>>>>>>>>>", String.valueOf(req.getEquipment_id()));
        Log.d("SOLICITA>>>>>>>>>>>>>>>", req.getSubject_matter());
        Log.d("SOLICITA>>>>>>>>>>>>>>>", req.getDescription());
    }

    private void searchDepartment() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Department.class, new DepartmentDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getApplicationContext(),gson);
        DepartmentInterface req = globalRetrofit.getRetrofit().create(DepartmentInterface.class);

        Call<List<Department>> getDepartments = req.getDepartments();

        getDepartments.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if(response.code() == 200) {
                     departments = response.body();
                    if(departments != null && departments.size() > 0) {
                        setDeparments_spinners(departments);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
            }
        });
    }

    private void searchSector(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Sector.class, new SectorDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getApplicationContext(), gson);
        SectorInterface req = globalRetrofit.getRetrofit().create(SectorInterface.class);

        Call<List<Sector>> getAllSectors = req.getAll();

        getAllSectors.enqueue(new Callback<List<Sector>>() {
            @Override
            public void onResponse(Call<List<Sector>> call, Response<List<Sector>> response) {
                if(response.code() == 200) {
                    sectors = response.body();
                    if((sectors != null) && (sectors.size() > 0)) {
                        setSectors_spinners(sectors);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Sector>> call, Throwable t) {
            }
        });
    }

    private void searchEquipmentsBySector(int sector_id){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Buscando equipamentos...");
        dialog.setCancelable(false);
        dialog.show();

        Gson gson = new GsonBuilder().registerTypeAdapter(Equipment.class, new EquipmentDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getApplicationContext(), gson);
        EquipmentInterface req = globalRetrofit.getRetrofit().create(EquipmentInterface.class);

        Call<List<Equipment>> getAllEquipments = req.getAll(sector_id);

        getAllEquipments.enqueue(new Callback<List<Equipment>>() {
            @Override
            public void onResponse(Call<List<Equipment>> call, Response<List<Equipment>> response) {
                if(dialog.isShowing()) dialog.dismiss();

                if(response.code() == 200) {
                    equipments = response.body();
                    if(equipments != null && equipments.size() > 0) {
                        setEquipments_spinners(equipments);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Equipment>> call, Throwable t) {
                if(dialog.isShowing()) dialog.dismiss();
            }
        });
    }

    private void setDeparments_spinners(List<Department> departments) {
        List<String> department_name = new ArrayList<>();

        for (Department department: departments) {
            department_name.add(department.getName());
        }

        ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department_name);
        adapterOpcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.deparments_spinners.setAdapter(adapterOpcoes);
    }

    private void setSectors_spinners(List<Sector> sectors) {
        List<String> sector_name = new ArrayList<>();

        for (Sector sector: sectors) {
            sector_name.add(sector.getName());
        }

        ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sector_name);
        adapterOpcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.sector_spinners.setAdapter(adapterOpcoes);
    }

    private void setEquipments_spinners(List<Equipment> equipments) {

        List<String> equipments_name = new ArrayList<>();

        for (Equipment equipment: equipments) {
            equipments_name.add(equipment.getDescription());
        }

        ArrayAdapter<String> adapterOpcoes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, equipments_name);
        adapterOpcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.equipments_spinner.setAdapter(adapterOpcoes);
    }

    private int getDepartmentSelect() {
        return this.departments.get(deparments_spinners.get)
    }

    private int getEquipmentSelect() {
        return 2;
    }


}
