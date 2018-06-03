package com.example.gbernardeau.gestionstock.METIER;

/**

 * <p>Nom de la classe : Article;</p>
 * <p>Ajoutée le 04/05/2018;</p>
 * Descriptif : Classe permettant de gérer un objet Article.

 */

public class Article {
    private String code;
    private String designation;
    private Integer idfam;

    /**
     * @param code est une chaîne de caractère
     * @param designation est une chaîne de caractère
     */
    public Article(String code, String designation, Integer idfam) {
        this.code = code;
        this.designation = designation;
        this.idfam = idfam;
    }

    /**
    Permet d'obtenir le code.
     */
    public String getCode() {
        return code;
    }
    /**
     Permet de définir le code.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     Permet d'obtenir la désignation.
     */
    public String getDesignation() {
        return designation;
    }
    /**
     Permet de définir la désignation.
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Permet de retourner une chaîne de caractère avec la valeur des attributs de Article.
     */
    public String toString(){
        return "Code : " + code + "; Désignation : " + designation;
    }

    public Integer getIdfam() {
        return idfam;
    }

    public void setIdfam(Integer idfam) {
        this.idfam = idfam;
    }

}
