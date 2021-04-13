package mg.mbds.nfcx_change.Model;

/**
 * Created by BillySycher on 20/06/2017.
 */

public class StringWithTag {
    public String string;
    private String id;
    private String nomColonne;
    public StringWithTag(String stringPart, String id,String nomColonne) {
        string = stringPart;
        setId(id);
        setNomColonne(nomColonne);
    }

    @Override
    public String toString() {
        return string;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomColonne() {
        return nomColonne;
    }

    public void setNomColonne(String nomColonne) {
        this.nomColonne = nomColonne;
    }
}
