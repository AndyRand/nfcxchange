package mg.mbds.nfcx_change.Service;
import java.util.List;

import mg.mbds.nfcx_change.Model.ClassMapTable;

/**
 * Created by BillySycher on 19/02/2019.
 */

public abstract class GenericAsyncTask {
    public abstract void execute(List<ClassMapTable> classMapTables);
    public GenericAsyncTask(){
    }
}
