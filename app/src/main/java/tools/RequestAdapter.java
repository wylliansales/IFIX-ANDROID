package tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import api.Response.Request;
import br.com.ifix.ifix.R;

public class RequestAdapter extends ArrayAdapter<Request> {

    private final Context ctx;
    private final List<Request> elementos;

    public RequestAdapter(Context ctx, List<Request> elementos) {
        super(ctx, R.layout.list_item, elementos);
        this.ctx = ctx;
        this.elementos = elementos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView nameDepartment = (TextView) rowView.findViewById(R.id.list_name_department);
        TextView nameSector = (TextView) rowView.findViewById(R.id.list_name_sector);

        nameDepartment.setText(elementos.get(position).getDepartment());
        nameSector.setText(elementos.get(position).getSubject_matter());

        return rowView;
    }
}
