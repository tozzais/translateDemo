package com.example.translatedemo;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * author : xumingming
 * data : 2022/3/30
 * description ：
 * email : 835683840@qq.com
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainA";

    //本地String文件的key
    List<StringKey> localStringXmlKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        AssetManager assetManager = getAssets();
       try {
           InputStream open = assetManager.open("strings.xml");
           localStringXmlKey = XMLParsingMethods.readXmlByPull(open);
           new ExcelDataLoader().execute("test4.xlt");
       }catch (Exception e){

       }


    }

    private ArrayList<TranslateModel> getXlsData(String xlsName, int index) {
        ArrayList<TranslateModel> countryList = new ArrayList();
        AssetManager assetManager = getAssets();
        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetRows = sheet.getRows();
            for (int i = 0; i < sheetRows; i++) {
                TranslateModel countryModel = new TranslateModel();
                countryModel.setName1(sheet.getCell(0, i).getContents());
                countryModel.setName2(sheet.getCell(1, i).getContents());
                countryModel.setName3(sheet.getCell(2, i).getContents());

                countryList.add(countryModel);
            }
            workbook.close();

        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }
        return countryList;
    }



    //在异步方法中 调用
    private class ExcelDataLoader extends AsyncTask<String, Void, ArrayList<TranslateModel>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<TranslateModel> doInBackground(String... params) {
            return getXlsData(params[0], 0);
        }

        @Override
        protected void onPostExecute(ArrayList<TranslateModel> countryModels) {
            LogUtils.e("一共"+countryModels.size()+"数据");
            getXlsData(countryModels);
        }
    }

    private void getXlsData(ArrayList<TranslateModel> list) {
        File file = new File(getCacheDir(), "string.txt");
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            fos.write("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>".getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.write("<resources>".getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            for (int i = 0; i < list.size(); i++) {
                if (i>= localStringXmlKey.size()){
                    break;
                }
                TranslateModel countryModel = list.get(i);
                StringKey stringKey = localStringXmlKey.get(i);
                fos.write(("<string name=\""+stringKey.getKey()+"\">"+countryModel.name3+"</string>").getBytes());
                fos.write(System.getProperty("line.separator").getBytes());
            }
            fos.write("</resources>".getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            LogUtils.e("生成成功");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

    }




}
