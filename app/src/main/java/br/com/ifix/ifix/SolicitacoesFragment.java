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

import java.util.ArrayList;
import java.util.List;

import api.HttpGlobalRetrofit;
import api.Response.Request;
import api.deserializers.RequestDes;
import api.interfaces.RequestInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.RequestAdapter;


public class SolicitacoesFragment extends Fragment {

    private ListView list;
    private List<Request> requests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitacoes, container, false);


        list = (ListView) view.findViewById(R.id.list);




        return view;
    }


    private void setList(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Request.class, new RequestDes()).create();
        HttpGlobalRetrofit globalRetrofit = new HttpGlobalRetrofit(getContext(), gson);
        RequestInterface req = globalRetrofit.getRetrofit().create(RequestInterface.class);

        Call<List<Request>> getRequests = req.getRequests();

        getRequests.enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if(response.code() == 200){
                    requests = response.body();
                    if(requests != null && requests.size() > 0) {
                        ArrayAdapter adapter = new RequestAdapter(getContext(), requests);
                        list.setAdapter(adapter);
                    }
                }
                Log.d("Solicitacoes >>>>>>>>>>", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {

            }
        });

    }

}
