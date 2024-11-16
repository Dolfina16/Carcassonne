package aed;

public class Handler{
    private Integer ref;

    public Handler(Integer r){
        ref = r;
    }

    public Integer ref(){
        return ref;
    }

    public void set_ref(int new_ref){
        ref = new_ref;
    }
}
