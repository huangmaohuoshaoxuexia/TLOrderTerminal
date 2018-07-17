package tl.com.tlsl.model.impl;

/**
 * Created by admin on 2017/10/27.
 */

public class ChoseAuditorImpl implements ChoseAuditorInterface{
    private ChoseAuditorInterface choseAuditorInterface;

    public ChoseAuditorInterface getChoseAuditorInterface() {
        return choseAuditorInterface;
    }

    public void setChoseAuditorInterface(ChoseAuditorInterface choseAuditorInterface) {
        this.choseAuditorInterface = choseAuditorInterface;
    }

    @Override
    public void choseAuditor(String name) {
        choseAuditorInterface.choseAuditor(name);
    }
}
