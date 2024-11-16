package xianhhh.Utils;

public class ValueSave {
    private Object[] values;

   public ValueSave(Object... value){
       this.values = value;
   }

   public Object get(int index){
       return values[index];
   }

    public Object getFirst(){
        return values[0];
    }

    public Object[] getValues() {
        return values;
    }
}
