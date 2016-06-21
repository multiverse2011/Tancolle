package org.t_robop.y_ogawara.tancolle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class UserRegisterActivity extends AppCompatActivity implements TextWatcher {
    private static final int REQUEST_GALLERY = 0;//ギャラリー選択で必要な初期化

    EditText edit_name;
    EditText edit_pho;
    EditText edit_twitter;
    EditText edit_days_ago;

    ImageView user_view;

    TextView user_birthday;

    TextView user_yearsold;

    InputMethodManager inputMethodManager;


    DatePickerDialog datePickerDialog;
    Calendar calendar;
    Calendar nowCale;

    int y;
    int m;
    int d;

    int birthday;

    //sppinerTest
    private Spinner spinnerCategory;

    private Spinner repetition;
    private String spinnerRepetitionItems[] = {"繰り返し通知無し", "毎日", "一週間毎","一ヶ月毎"};


    private AlertDialog Adddialog;

    //EditText用変数群
    View viewV;
    LayoutInflater inflater;
    EditText editText;

    ArrayAdapter<String> adapter;


    EditText edit_memo;


    String TryText;
    int TryFlag;

    Bitmap img;
    String img_name;

    CheckBox tamura_check;
    CheckBox yesterday_check;
    CheckBox today_check;

    int ta_check;
    int ye_check;
    int to_check;



    /////////////////////////Override/////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //数多（予定）の宣言
        repetition=(Spinner)findViewById(R.id.spinner2);

        //Spinner設定
        sppinerCategory();
        sppinerSet(repetition,spinnerRepetitionItems);

        user_yearsold=(TextView)findViewById(R.id.YearsOld);
        //年齢表示
        if(YearsOldSet(birthday)>1000) {
            user_yearsold.setText("");
        }
        else {
            user_yearsold.setText(String.valueOf(YearsOldSet(birthday))+"歳");
        }

        edit_name=(EditText)findViewById(R.id.EditName);
        // EditText が空のときに表示させるヒントを設定
        edit_name.setHint("Name");

        edit_pho=(EditText)findViewById(R.id.EditPho);
        // EditText が空のときに表示させるヒントを設定
        edit_pho.setHint("Phonetic");

        edit_twitter=(EditText)findViewById(R.id.twitter);
        // EditText が空のときに表示させるヒントを設定
        edit_twitter.setHint("@twitter");

        edit_days_ago=(EditText)findViewById(R.id.ago);
        // EditText が空のときに表示させるヒントを設定
        edit_days_ago.setHint("Days");

        edit_memo=(EditText)findViewById(R.id.Memo);
        // EditText が空のときに表示させるヒントを設定
        edit_memo.setHint("何でも自由に書いてね！");

        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //EditTextの内容設定
        EditTextSet(edit_name);
        EditTextSet(edit_pho);
        EditTextSet(edit_twitter);
        EditTextSet(edit_days_ago);

        //ImageViewの初期設定
        user_view= (ImageView)findViewById(R.id.userview);
        user_view.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //CheckBox初期宣言
        tamura_check=(CheckBox)findViewById(R.id.TamuraCheck);
        yesterday_check=(CheckBox)findViewById(R.id.YesterdayCheck);
        today_check=(CheckBox)findViewById(R.id.TodayCheck);
        //CheckBoxの値を取得
        TamuraJudge(tamura_check);
        YesterdayJudge(yesterday_check);
        TodayJudge(today_check);


        user_birthday=(TextView)findViewById(R.id.userbirthday);


        //誕生日初期設定
        BirthTodaySet();

        //誕生日描画
        BirthDayDraw(birthday);


        inflater = LayoutInflater.from(UserRegisterActivity.this);
        viewV = inflater.inflate(R.layout.dialog_user_register, null);
        editText = (EditText)viewV.findViewById(R.id.editText1);


        edit_name.addTextChangedListener(this);//aaaaaaaaaaaaaa


        //画像読み込み
        InputStream in;
        try {
            in = openFileInput(img_name+".jpg");
            img = BitmapFactory.decodeStream(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        user_view.setImageBitmap(img);
    }

    //画像をドキュメントから選択からのImageViewセット
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO Auto-generated method stub
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap pct = BitmapFactory.decodeStream(in);

                // 作られたサムネイルから横幅とそれに応じた高さを指定
                int pctWidth = pct.getWidth();
                int pctHeight = pct.getHeight();

                //1000以上であれば5で割らせる
                if(pctWidth>1000||pctHeight>1000) {
                    pctWidth = pctWidth / 3;
                    pctHeight = pctHeight / 3;
                }
                else//1000以下であれば2で許す
                {
                    pctWidth = pctWidth / 2;
                    pctHeight = pctHeight / 2;
                }

                // bitmapの画像を元の比率から最適化された比率で作成する
                img = Bitmap.createScaledBitmap(pct, pctWidth, pctHeight, false);
                in.close();

                // 選択した画像を表示
                user_view.setImageBitmap(img);
                } catch (Exception e) {

                }



            //画像保存時の名前用の現在日時取得
            calendar = Calendar.getInstance();
            int imgye,imgmo,imgda,imgho,imgmi,imgse;

            imgye = calendar.get(Calendar.YEAR); // 年
            imgmo = calendar.get(Calendar.MONTH); // 月
            imgmo++;
            imgda = calendar.get(Calendar.DAY_OF_MONTH); // 日
            imgho=calendar.get(Calendar.HOUR_OF_DAY);//時
            imgmi=calendar.get(Calendar.MINUTE);//分
            imgse=calendar.get(Calendar.SECOND);//秒

            img_name=String.valueOf(imgye)+String.valueOf(imgmo)+String.valueOf(imgda)+String.valueOf(imgho)+String.valueOf(imgmi)+String.valueOf(imgse);


            //ローカルファイルへ保存
            try{
                final FileOutputStream out = openFileOutput(img_name+".jpg",Context.MODE_WORLD_READABLE);
                Log.d("aaaa",img_name);
                img.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }

            }
        }


    //TextWatcher
    @Override
    public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
        //操作前のEtidTextの状態を取得する
        edit_pho.setText(s.toString());


        //EditTextにリスナーをセット
        edit_name.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() != KeyEvent.ACTION_DOWN)){


                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //操作中のEtidTextの状態を取得する
    }

    @Override
    public void afterTextChanged(Editable s) {
        //操作後のEtidTextの状態を取得する
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // アクションバー内に使用するメニューアイテムを注入します。
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_register_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //アクションバー処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // アクションバーアイテム上の押下を処理します。
        switch (item.getItemId()) {
            case R.id.action_button:
                AllRegist();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    ////////////////////////////////////////////////////////////




    ////////////////////クリック処理群/////////////////////////

    public void EditName(View v)
    {
        //フォーカスon
        EditTextClick(edit_name);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditPho(View v)
    {
        //フォーカスon
        EditTextClick(edit_pho);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditTwitter(View v)
    {
        //フォーカスon
        EditTextClick(edit_twitter);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public void EditAgo(View v)
    {
        //フォーカスon
        EditTextClick(edit_days_ago);
        //キーボード表示
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    //画像クリック時
    public void UserView(View v)
    {
        // ギャラリー呼び出し
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    //誕生日クリック時
    public void BirthDay(View v)
    {
        DatePickerSet();
        // 日付設定ダイアログの表示
        datePickerDialog.show();
    }

    //カテゴリ追加
    public void CategoryPlus(View v)
    {
        SpinnerCategoryAdd();
        Adddialog.show();
    }


    //////////////////////////////////////////////////




    ////////////////////自作関数群////////////////////

    public void EditSetString(EditText edit,String string)
    {
        String text;
        // エディットテキストのテキストを全選択します
        edit.selectAll();
        // エディットテキストのテキストを取得します
        text = edit.getText().toString();
        //editTextの初期化
        edit.getEditableText().clear();

        //textの長さが0(何も入っていない)の場合
        if(text.length()==0)
        {

        }
        else
        {
            //textから数値のみ取り出す
            int ret = Integer.parseInt(text.replaceAll("[^0-9]",""));
            //「日前」を加えて表示
            edit.setText(String.valueOf(ret)+string);
        }
    }


    public void EditTextSet(final EditText edit)
    {
        //EditTextにリスナーをセット
        edit.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    EditSetString(edit_days_ago,"日前");//edit_days_ago用処理

                    //フォーカスを外す
                    edit.setFocusable(false);
                    edit.setFocusableInTouchMode(false);
                    edit.requestFocus();

                    // エディットテキストのテキストを全選択します
                    edit.selectAll();

                    return true;
                }

                return false;
            }
        });
    }

    //指定されたEditTextがクリックされた時にフォーカスを移す処理
    public void EditTextClick(EditText edit)
    {
        //指定されたEditTextのフォーカスをonへ
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        //edit.getEditableText().clear();//editTextの初期化
    }


    //カテゴリー追加用spinner
    public void sppinerCategory()
    {

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加します
        adapter.add("<未選択>");
        spinnerCategory = (Spinner) findViewById(R.id.spinner1);
        // アダプターを設定します
        spinnerCategory.setAdapter(adapter);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //処理


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //spinner使おうぜ！！（セットするspinner,セットするTextView,セットするString）
    public void sppinerSet(Spinner nSpinner, final String spinnerItems[])
    {
        // ArrayAdapter
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner に adapter をセット
        nSpinner.setAdapter(adapter);

        // リスナーを登録
        nSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                if (item.equals(spinnerItems[1]))
                {
                }
                else if (item.equals(spinnerItems[2]))
                {

                }
                else if (item.equals(spinnerItems[3]))
                {

                }
                else //初期値
                {

                }
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //現在の日付の取得
    public void BirthTodaySet()
    {
        ////////// 日付情報の初期設定 //////////
        calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR); // 年
        m = calendar.get(Calendar.MONTH); // 月
        d = calendar.get(Calendar.DAY_OF_MONTH); // 日

        nowCale=calendar;

        birthday=BirthDayGet(y,m,d);

        ////////////////////////////////////////
    }

    //DatePickerの設定
    public void DatePickerSet()
    {
        // 日付設定時のリスナ作成
        //ok押した時の処理
        DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
            //okボタンを押したときの年月日を取得できる
            public void onDateSet(DatePicker datePicker, int year,
                                  int monthOfYear, int dayOfMonth) {
                //押されてる日時を変数へ
                y = year;
                m = monthOfYear;
                d = dayOfMonth;

                //birthCale.set(y,m,d);

                birthday=BirthDayGet(y,m,d);

                //描画
                BirthDayDraw(birthday);

                //年齢表示
                if(YearsOldSet(birthday)>1000) {
                    user_yearsold.setText("");
                }
                else {
                    user_yearsold.setText(String.valueOf(YearsOldSet(birthday))+"歳");
                }
            }
        };
        // 日付設定ダイアログの作成・リスナの登録
        datePickerDialog = new DatePickerDialog(this, DateSetListener, y, m, d);
    }


    //カレンダーデータを八桁の数値へ
    public int BirthDayGet(int Year,int Month,int Day)
    {
        int caly,calm;

        caly=Year*10000;
        calm=Month*100;

        return caly+calm+Day;
    }

    //八桁の数値から算出してTextViewに描画
    public void BirthDayDraw(int Birthday)
    {
        int caly,calm,cald,cal;

        caly=Birthday/10000;

        calm=Birthday/100;
        cal=caly*100;
        calm=calm-cal;

        cal=caly*10000;
        cald=Birthday-cal;
        cal=calm*100;
        cald=cald-cal;

        y=caly;
        m=calm;
        d=cald;

        cal=m+1;

        user_birthday.setText(y+"/"+cal+"/"+d);
    }

    //spinnerCategory追加処理
    public void SpinnerCategoryAdd()
    {
        if(Adddialog == null)//Adddialogが作成されていない時
        {


            Adddialog = new AlertDialog.Builder(UserRegisterActivity.this)
                    .setTitle("カテゴリー入力")//DiaLogタイトル
                    .setView(viewV)//View指定
                    //DiaLog内の決定を押した時の処理
                    .setPositiveButton("決定", new DialogInterface.OnClickListener() //ボタン作成
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //クリック時の処理

                            //入力値取得用変数
                            String addcategory;

                            // エディットテキストのテキストを全選択します
                            editText.selectAll();

                            //editTextに何も入力されてない時
                            if (editText.getText().toString().equals("")) {
                                addcategory = "";//0が入る
                            } else {
                                // エディットテキストのテキストを取得します
                                addcategory = editText.getText().toString();
                            }

                            //要素追加
                            //リストとadaptorに入力値を入れる
                            adapter.add(addcategory);

                            //adapter更新
                            adapter.notifyDataSetChanged();

                            editText.getEditableText().clear();//editTextの初期化

                            spinnerCategory.setSelection(adapter.getPosition(addcategory));

                        }
                    })
                    .create();//初回AddDiaLog制作
        }
    }

    //年齢計算
    public int YearsOldSet(int birth)
    {
        int birthyear,nowyear,nowmonth,nowday,yearsold;
        int caly,calm,cald,cal;

        caly=birth/10000;

        calm=birth/100;
        cal=caly*100;
        calm=calm-cal;

        cal=caly*10000;
        cald=birth-cal;
        cal=calm*100;
        cald=cald-cal;


        calendar = Calendar.getInstance();

        nowyear = calendar.get(Calendar.YEAR); // 年
        nowmonth = calendar.get(Calendar.MONTH); // 月
        nowday = calendar.get(Calendar.DAY_OF_MONTH); // 日

        birthyear=birth/10000;

        yearsold=nowyear-birthyear;

        if(calm>nowmonth)
        {
            yearsold=yearsold-1;
        }
        else if(calm==nowmonth&&cald>nowday)
        {
            yearsold=yearsold-1;
        }

        return yearsold;
    }


    //TamuraCheck判定処理
    public void TamuraJudge(final CheckBox check)
    {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true)
                {
                    ta_check=1;
                }
                else
                {
                    ta_check=0;
                }
            }
        });
    }
    //YesterdayCheck判定処理
    public void YesterdayJudge(final CheckBox check)
    {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true)
                {
                    ye_check=1;
                }
                else
                {
                    ye_check=0;
                }
            }
        });
    }
    //TodayCheck判定処理
    public void TodayJudge(final CheckBox check)
    {
        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                // チェックボックスのチェック状態を取得します
                boolean checked = check.isChecked();

                if(checked==true)
                {
                    to_check=1;
                }
                else
                {
                    to_check=0;
                }
            }
        });
    }


    public void AllRegist()
    {
        //Data型の宣言
        Data allData =new Data();
        //Data型にデータをセット
        allData.setName("西村");
        allData.setKana("にしむら");
        allData.setBirthday(19970616);
        allData.setCategory("友達");
        allData.setTwitterID("Taiga_Natto");
        allData.setMemo("教科書を見て実装して欲しい");
        allData.setImage("Imageデータ");
        allData.setSmallImage("Imageデータ");
        allData.setTamura(1);
        allData.setNotif_yest(1);
        allData.setNotif_today(1);
        allData.setNotif_day(3);
        allData.setNotif_recy(3);
        //dbに書き込み
        dbAssist.insertData(allData,this);
    }

    //////////////////////////////////////////////////

}
