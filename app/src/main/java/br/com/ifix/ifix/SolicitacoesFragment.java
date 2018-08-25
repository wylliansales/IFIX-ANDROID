package br.com.ifix.ifix;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import api.HttpGlobalRetrofit;
import api.Response.Request;
import api.Response.ResponseGlobal;
import api.deserializers.GlobalDes;
import api.deserializers.RequestDes;
import api.interfaces.RequestInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Notification;
import tools.RequestAdapter;


public class SolicitacoesFragment extends Fragment {

    private ListView list;
    private ArrayList<Request> requests = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacoes, container, false);
        list = (ListView) view.findViewById(R.id.list_requests);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setList();
    }

    private void setList(){
        Gson gson = new GsonBuilder().registerTypeAdapter(ResponseGlobal.class, new GlobalDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getContext(), gson);
        RequestInterface req = globalRetrofit.getRetrofit().create(RequestInterface.class);

        Call<ResponseGlobal> getRequests = req.getRequestAll();

        getRequests.enqueue(new Callback<ResponseGlobal>() {
            @Override
            public void onResponse(Call<ResponseGlobal> call, Response<ResponseGlobal> response) {
                if(response.code() == 200){
                    JsonArray json = response.body().getData().getAsJsonArray();

                    for(int i=0; i < json.size(); i++) {
                        requests.add(new Gson().fromJson(json.get(i), Request.class));
                    }

                    if(requests != null && requests.size() > 0) {
                        ArrayAdapter adapter = new RequestAdapter(getContext(), requests);
                        list.setAdapter(adapter);
                        Notification.notify(getContext(),  String.valueOf(requests.size()) + " solicitações encontradas",0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGlobal> call, Throwable t) {
                Log.d("Solicitacoes >>>>>>>>>>", t.getMessage());
            }
        });
    }

}
