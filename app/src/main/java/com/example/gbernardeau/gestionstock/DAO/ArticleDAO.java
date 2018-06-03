package com.example.gbernardeau.gestionstock.DAO;
/**

 * <p>Nom de la classe : ArticleDAO;</p>
 * <p>Ajoutée le 29/05/2018;</p>
 * Descriptif : Classe permettant de gérer un objet ArticleDAO.

 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gbernardeau.gestionstock.METIER.Article;
import com.example.gbernardeau.gestionstock.METIER.Fiches;

import java.util.ArrayList;

public class ArticleDAO extends DAO<Article> {
    private SQLiteGestionStock dbGestionStock;

    private static final String Table_ARTICLE = "ARTICLE";
    private static final String COL_ID_ARTICLE = "CODE";
    private static final String COL_DESIGNATION_ARTICLE = "DESIGNATION";
    private static final String COL_ID_FAMILLE = "IDFAM";
    private static final String COL_ID_EMPLACEMENT = "IDEMP";
    private static final String COL_STOCK_ARTICLE = "STOCK";

    private SQLiteDatabase db; //Dans le cas présent, db sera la base de donnée SQLite.

    /**
     * Les constantes sont définis avec les valeurs correspondantes.
     */

    /**
     * Permet d'instancier un nouvel objet de SQLiteGestionStock en tenant compte du context passé en paramètre.
     * @param context
     */
    public ArticleDAO(Context context) {
        dbGestionStock = new SQLiteGestionStock(context);
    }

    /**
     * Permet d'ouvrir la base de données.
     */
    public void open() {
        db = dbGestionStock.getWritableDatabase();
    }

    /**
     * Permet de fermer la connection à la base de données.
     */
    public void close() {
        db.close();
    }

    /**
     * Permet d'effectuer un INSERT en tenant compte de ma qui est un objet d'Article.
     * En fonction du résultat, un booléen sera retourné.
     * @param ma
     * @return res
     */
    public boolean insert(Article ma) {
        boolean res;
        long insert;
        res = false;
        ContentValues mavaleur = new ContentValues();
        mavaleur.put(COL_DESIGNATION_ARTICLE, ma.getDesignation());
        insert = db.insert(Table_ARTICLE, null, mavaleur);
        if (insert != -1) {
            res = true;
        }
        return res;
    }
    public Article create(Article ma) {
        ContentValues valeursSQL = new ContentValues();
        valeursSQL.put(COL_DESIGNATION_ARTICLE, ma.getDesignation());
        valeursSQL.put(COL_ID_ARTICLE, ma.getCode());
        valeursSQL.put(COL_ID_FAMILLE, ma.getIdfam());

        db.insert(Table_ARTICLE, null, valeursSQL);
        return null;
    }
    /**
     * Permet d'effectuer un UPDATE en tenant compte de obj qui est un objet de Article, passé en paramètre.
     * La fonction retourne un booléen en fonction du résultat.
     * @param obj
     * @return res
     */
    public boolean update(Article obj) {
        boolean res;
        int update;
        res = false;
        ContentValues mavaleur = new ContentValues();
        mavaleur.put(COL_DESIGNATION_ARTICLE, obj.getDesignation());
        update = db.update(Table_ARTICLE, mavaleur, null, null);
        if (update > 0) {
            res = true;
        }
        return res;
    }

    public String selectlibFamille(int id) {
        Cursor res;
        res = db.rawQuery("SELECT FAMILLE.LIBELLE  FROM FAMILLE INNER JOIN "+ Table_ARTICLE +" ON FAMILLE.ID = ARTICLE.IDFAM WHERE ARTICLE.IDFAM = " + id, null);
        res.moveToFirst();
        return res.getString(0);
    }
    public Cursor getidFamille() {
        Cursor res;
        res = db.rawQuery("SELECT ID, LIBELLE FROM FAMILLE ", null);
        res.moveToFirst();
        return res;
    }

    /**
     * Permet d'effectuer un DELETE en tenant compte de obj qui est un objet de Article, passé en paramètre.
     * La fonction retourne un booléen en fonction du résultat.
     * @param obj
     * @return res
     */
    public Article delete(Article obj) {
        boolean res;
        int delete;
        res = false;
        delete = db.delete(Table_ARTICLE, null, null);
        if (delete > 0) {
            res = true;
        }
        return null;
    }

    /**
     * Permet d'effectuer un READ par le biais d'un curseur qui permet d'intéragir avec les résultats d'une requête SQL.
     * Id est passé en paramètre et la fonction retourne unArticle de la classe Article.
     * @param id
     * @return unArticle
     */
    public Article read(long id) {
        Cursor res = db.query(Table_ARTICLE, null, null, null, null, null, null);
        Article unArticle = new Article(res.getString(0), res.getString(1), res.getInt(2));
        return unArticle;
    }

    /**
     * Fonction retournant un ArrayList peuplé de curseur ayant des données qui sont ajoutées à chaque tour de boucle.
     * @return listArticle
     */
    public ArrayList<Article> read() {
        ArrayList<Article> listArticle = new ArrayList<Article>();
        String code;
        String lib;
        Integer idfam;
        Integer idemp;
        Integer stock;

        Article ma;
        Cursor res;
        res = db.query(Table_ARTICLE, null, null, null, null, null, null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            code = res.getString(0);
            lib = res.getString(1);
            idfam = res.getInt(2);
            ma = new Article(code, lib, idfam);
            listArticle.add(ma);
            res.moveToNext();
        }

        return listArticle;
    }

    /**
     * Fonction retournant un ArrayList peuplé de curseur ayant des données qui sont ajoutées à chaque tour de boucle.
     * @return listArticle
     */
    public ArrayList<Article> readDesc() {
        ArrayList<Article> listArticle = new ArrayList<Article>();
        String code;
        String lib;
        Integer stock;
        Integer idfam;
        Integer idemp;
        Article ma;
        Cursor res;
        res = db.query(Table_ARTICLE, null, null, null, "desc", null, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            code = res.getString(0);
            lib = res.getString(1);
            idfam = res.getInt(2);
            ma = new Article(code, lib, idfam);
            listArticle.add(ma);
        }

        return listArticle;
    }
    public int getLastIdFiches(){
        int res;
        Cursor C = db.query(Table_ARTICLE, null, null, null,null,null,null);
        res = C.getCount();
        C.close();
        return res;
    }

    public Article readAtCursor(int id){
        Article retArticle = null;
        Cursor C = db.query(Table_ARTICLE, null, null, null,null,null,null);
        C.moveToFirst();
        if(C.moveToPosition(id)){
            retArticle = new Article(C.getString(0), C.getString(1), C.getInt(2));
        }
        C.close();
        return retArticle;
    }

}