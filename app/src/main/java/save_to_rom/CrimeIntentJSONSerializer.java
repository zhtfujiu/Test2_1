package save_to_rom;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import pack.Crime;

/**
 * Created by 昊天 on 2016/6/3.
 */
public class CrimeIntentJSONSerializer {
    private Context context;
    private String mFileName;

    public CrimeIntentJSONSerializer(Context context, String mFileName){
        this.context=context;
        this.mFileName=mFileName;
    }

    public ArrayList<Crime> LoadCrimes() throws IOException {
        ArrayList<Crime> crimes=new ArrayList<Crime>();
        BufferedReader reader=null;

        try {
            InputStream in=context.openFileInput(mFileName);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString=new StringBuilder();
            String line=null;
            while ((line=reader.readLine())!=null){
                jsonString.append(line);
            }
            JSONArray array=(JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i=0;i<array.length();i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if (reader!=null){
                reader.close();
            }
        }
        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws IOException {
        JSONArray jsonArray=new JSONArray();
        for (Crime c:crimes){
            jsonArray.put(c.toJSON());
        }
        Writer writer=null;
        try {
            OutputStream out=context.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                writer.close();
            }
        }
    }
}
