package Efectura.utilities;

import Efectura.pages.AssignedRecordPage;
import Efectura.pages.FormsPage;
import Efectura.pages.GeneralPage;
import Efectura.pages.SearchPage;

public class Pages {

    private SearchPage searchPage;
    private GeneralPage generalPage;
    private FormsPage formsPage;
    private AssignedRecordPage assignedRecordPage;



    public Pages() {
        this.searchPage = new SearchPage();
        this.generalPage = new GeneralPage();
        this.formsPage = new FormsPage();
        this.assignedRecordPage = new AssignedRecordPage();

    }


    public SearchPage searchPage() {return searchPage;}
    public GeneralPage generalPage() {return generalPage;}
    public FormsPage formsPage() {return formsPage;}
    public AssignedRecordPage assignedRecordPage() {return assignedRecordPage;}
}
