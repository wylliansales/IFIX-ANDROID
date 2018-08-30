package br.com.ifix.ifix;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

import api.HttpGlobalRetrofit;
import api.Response.Request;
import api.Response.ResponseGlobal;
import api.deserializers.GlobalDes;
import api.interfaces.RequestInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Notification;
import tools.RequestAdapter;


public class AbertosFragment extends Fragment {

    private ListView list;
    private ArrayList<Request> requests = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_abertos, container, false);
        list = (ListView) view.findViewById(R.id.requests_aberto);
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

        Call<ResponseGlobal> getRequests = req.getRequestOpen();

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
