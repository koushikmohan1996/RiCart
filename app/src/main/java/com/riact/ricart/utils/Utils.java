package com.riact.ricart.utils;

/**
 * Created by koushik on 3/12/17.
 */

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static int getIndexOfItem(String group,String itemCode){

        int val =0;

        for(Model model:Constants.itemMap.get(group)){

            if(model.getItemCode().equals(itemCode)){
                val = model.getIndex();
            }


        }

        return val;
    }

    public static float getPriceOfItem(String group,String itemCode){

        float val =0;

        for(Model model:Constants.itemMap.get(group)){

            if(model.getItemCode().equals(itemCode)){
                val = model.getPrice();
            }


        }

        return val;
    }
}