package com.example.gbernardeau.gestionstock.IHM;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gbernardeau.gestionstock.DAO.ArticleDAO;
import com.example.gbernardeau.gestionstock.METIER.Article;

import com.example.gbernardeau.gestionstock.R;

import java.util.ArrayList;

public class ArticlesAdapter extends ArrayAdapter<Article> {
    String libfamille;
    Context context;
    int layoutResourceId;
    ArticleDAO DAOA;
    ArrayList<Article> listArticle = null;

    public ArticlesAdapter(Context context, int layoutResourceId, ArrayList<Article> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.listArticle = data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ArticleHolder holder = null;
        DAOA = new ArticleDAO(this.getContext());
        DAOA.open();
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ArticlesAdapter.ArticleHolder();
            holder.code = (TextView)row.findViewById(R.id.article_code);
            holder.designation = (TextView)row.findViewById(R.id.article_designation);
            holder.famille = (TextView)row.findViewById(R.id.article_famille);
            row.setTag(holder);
        }
        else
        {
            holder = (ArticlesAdapter.ArticleHolder)row.getTag();
        }
        Article articles = listArticle.get(position);
        //Log.v("req", articles.getCode());
        libfamille = DAOA.selectlibFamille(articles.getIdfam()).toString();

        holder.code.setText(articles.getCode());
        holder.designation.setText(articles.getDesignation());
        holder.famille.setText(libfamille);

        return row;
    }

    static class ArticleHolder
    {
        TextView code;
        TextView designation;
        TextView famille;
    }

}
