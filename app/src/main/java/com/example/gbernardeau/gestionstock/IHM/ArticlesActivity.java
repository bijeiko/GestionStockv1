package com.example.gbernardeau.gestionstock.IHM;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gbernardeau.gestionstock.DAO.ArticleDAO;
import com.example.gbernardeau.gestionstock.METIER.Article;
import com.example.gbernardeau.gestionstock.R;

import java.util.ArrayList;

public class ArticlesActivity extends AppCompatActivity {
    private Button Accueilbtn, ToutslesFiches;
    private ListView Listarticle;
    private ArticleDAO DAOA;
    private String libemp;

    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id){
        return false;
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        setTitle("Gestion des Stocks : Gestion des Articles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ad14aa")));
        DAOA = new ArticleDAO(this.getApplicationContext());
        DAOA.open();
        refrech_list();
        // Bouton navigation
        Accueilbtn = (Button) findViewById(R.id.Accueilbtn);
        Accueilbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activitiemain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activitiemain);
            }
        });

        ToutslesFiches = (Button) findViewById(R.id.ToutslesFiches);
        ToutslesFiches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityfiches = new Intent(getApplicationContext(), FichesActivity.class);
                startActivity(activityfiches);
            }
        });
    }

    public void refrech_list(){

        ListView listContent = (ListView)findViewById(R.id.eLVArticle);
        ArticlesAdapter adapter = new ArticlesAdapter(this, R.layout.list_articles, DAOA.read());
        listContent.setAdapter(adapter);
        listContent.setOnItemClickListener(OnClickItem);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_ajouter:
                AlertAjout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AlertAjout(){
        LayoutInflater inflater = getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.dialog_ajout_articles, null);
        AlertDialog.Builder Builder = new AlertDialog.Builder(this);


        // set title
        Builder.setTitle("Ajouter une matiere");

        // set dialog message
        Builder.setView(promptsView);
        final EditText ETdesignation = (EditText) promptsView.findViewById(R.id.ETdesignation);
        final EditText Tcode = (EditText) promptsView.findViewById(R.id.Tcode);
        final Spinner Sfamille = (Spinner) promptsView.findViewById(R.id.Sfamille);

        // ajout contenu spinner Famille
        ArrayList<String> Listarticle = new ArrayList<String>();
        ArrayAdapter<String> adapterarticle;
        Cursor carticle= DAOA.getidFamille();
        adapterarticle= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Listarticle);
        Listarticle.clear();
        while (!carticle.isAfterLast()) {
            String libetat= carticle.getString(1).toString();
            Listarticle.add(libetat);
            carticle.moveToNext();
        }
        Sfamille.setAdapter(adapterarticle);
        Sfamille.setSelection(0, true);
        View varticle = Sfamille.getSelectedView();
        ((TextView)varticle).setTextColor(Color.BLACK);

        Builder
                .setMessage("Ajouter un article")
                .setCancelable(true)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Context context = ArticlesActivity.this;
                        int duration = Toast.LENGTH_LONG;

                        if(!(ETdesignation.getText().length() == 0) && !(Tcode.getText().length() == 0)){

                            Article ma= new Article(Tcode.getText().toString(),ETdesignation.getText().toString(), Sfamille.getId());
                            DAOA.create(ma);
                            refrech_list();
                            CharSequence text = "Article Ajouté!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            CharSequence text = "Champs vide!";
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }

                    }

                })
                .setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = Builder.create();

        // show it
        alertDialog.show();

    }

    public AdapterView.OnItemClickListener OnClickItem = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Article cl = DAOA.readAtCursor(position);
            LayoutInflater inflater = getLayoutInflater();

            View promptsView = inflater.inflate(R.layout.dialog_ajout_articles, null);
            AlertDialog.Builder Builder = new AlertDialog.Builder(ArticlesActivity.this);

            // set title
            Builder.setTitle("Modifier/Supprimer une fiche");
            // set dialog message
            Builder.setView(promptsView);
            final EditText ETdesignation = (EditText) promptsView.findViewById(R.id.ETdesignation);
            final EditText Tcode = (EditText) promptsView.findViewById(R.id.Tcode);
            final Spinner Sfamille = (Spinner) promptsView.findViewById(R.id.Sfamille);

            ETdesignation.setText(cl.getDesignation());
            Tcode.setText(cl.getCode());


            // ajout contenu spinner Famille
            ArrayList<String> Listarticle = new ArrayList<String>();
            ArrayAdapter<String> adapterarticle;
            Cursor carticle= DAOA.getidFamille();
            adapterarticle= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Listarticle);
            Listarticle.clear();
            while (!carticle.isAfterLast()) {
                String libetat= carticle.getString(1).toString();
                Listarticle.add(libetat);
                carticle.moveToNext();
            }
            Sfamille.setAdapter(adapterarticle);
            Sfamille.setSelection(0, true);
            View varticle = Sfamille.getSelectedView();
            ((TextView)varticle).setTextColor(Color.BLACK);

            Builder
                    .setMessage("Modifier une article")
                    .setCancelable(true)
                    .setPositiveButton("Modifier",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;
                            if(!(ETdesignation.getText().length() == 0) && !(Tcode.getText().length() == 0)){
                                Article ma= new Article( cl.getCode() ,ETdesignation.getText().toString(), Sfamille.getId());
                                DAOA.update(ma);
                                refrech_list();

                                CharSequence text = "Article modifiée!";
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }else{
                                CharSequence text = "Champs vide!";
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }

                    })
                    .setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    })
                    .setNeutralButton("Supprimer",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            AlertDialog.Builder Builder = new AlertDialog.Builder(ArticlesActivity.this);
                            // set title
                            Builder.setTitle("Voulez-vous vraiment supprimer cette Article ?");

                            // set dialog message
                            Builder
                                    .setMessage("Supprimer une fiche")
                                    .setCancelable(true)
                                    .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            Context context = getApplicationContext();
                                            int duration = Toast.LENGTH_LONG;
                                            Article ma= new Article( cl.getCode() ,ETdesignation.getText().toString(), Sfamille.getId());
                                            DAOA.delete(ma);
                                            refrech_list();

                                            CharSequence text = "Article supprimée!";
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }
                                    })
                                    .setNegativeButton("Non",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = Builder.create();

                            // show it
                            alertDialog.show();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = Builder.create();

            // show it
            alertDialog.show();


        }

    };
}
