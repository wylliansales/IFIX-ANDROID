package tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import api.Response.Request;
import br.com.ifix.ifix.R;

public class RequestAdapter extends ArrayAdapter<Request> {

    private final Context ctx;
    private final ArrayList<Request> elementos;

    public RequestAdapter(Context ctx, ArrayList<Request> elementos) {
        super(ctx, R.layout.list_item, elementos);
        this.ctx = ctx;
        this.elementos = elementos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView nameDepartment = (TextView) rowView.findViewById(R.id.list_name_department);
        TextView status = (TextView) rowView.findViewById(R.id.list_status);
        TextView date = (TextView) rowView.findViewById(R.id.list_data);
        TextView description = (TextView) rowView.findViewById(R.id.list_description);
        TextView subject_matter = (TextView) rowView.findViewById(R.id.list_subject_matter);


        nameDepartment.setText(elementos.get(position).getDepartment());
        status.setText((!elementos.get(position).isFinalized()) ? "Aberto" : "Finalizado");
        date.setText(elementos.get(position).getCreated_at());
        description.setText(elementos.get(position).getDescription());
        subject_matter.setText(elementos.get(position).getSubject_matter());

        return rowView;
    }
}
