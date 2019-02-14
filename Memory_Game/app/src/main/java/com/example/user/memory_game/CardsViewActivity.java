package com.example.user.memory_game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.user.memory_game.GameActivity.getRoundedCornerBitmap;


public class CardsViewActivity extends Activity {

    public static final String FRONT_CARDS = "front_cards";
    public static final String BACK_CARDS = "back_cards";
    public static final int REQUEST_IMAGE_CAPTURE = 123;
    public static final String TAG = "gitit";
    public static final String PATH_FILE = "path_file";
    public static final String PATH_KEY = "path_key";
    public static final String SETTINGS_FILE = "settings_file";
    public static final String DELIMITER = "delimiter";
    public static final String DRAWABLE = "drawable";
    public static final String PHOTO = "photo";
    private ViewPager viewPager;
    private List<Integer> layoutsRes;
    private String photoPath;
    private GridView gridView;
    private List<MyBitmap> bitmapsImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_view);
        viewPager = findViewById(R.id.viewPager);
        gridView = findViewById(R.id.gridView);
        loadBitmapImagesFromSharedPreferences();

        boolean isBackCards = getIntent().getBooleanExtra(SettingsActivity.IS_BACK_CARDS, false);
        layoutsRes = new ArrayList<>();
        if (isBackCards) {
            layoutsRes.add(R.layout.back_card_1);
            layoutsRes.add(R.layout.back_card_2);
            layoutsRes.add(R.layout.back_card_3);
            layoutsRes.add(R.layout.add_back_card_layout);
        } else {
            layoutsRes.add(R.layout.disney_cards);
            layoutsRes.add(R.layout.area_cards);
        }
        ListCardsAdapter listCardsAdapter = new ListCardsAdapter(this);
        viewPager.setAdapter(listCardsAdapter);

    }
    public void btnChoose(View view) {
        String tag = (String) view.getTag();
        setSettings(FRONT_CARDS, tag);
    }
    public void btnChooseBackCards(View view) {
        String typeAndTag = DRAWABLE + DELIMITER + view.getTag();
        setSettings(BACK_CARDS, typeAndTag);

    }
    private void setSettings(String key, String value){
        getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)
                .edit().
                putString(key, value)
                .apply();
        finish();
    }
    public void btnAddBackCard(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile
                        (this, "com.example.user.memory_game.fileprovider", photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            savePathToSharedPreferences();
            Bitmap bitmap = createBitmap(photoPath);
            addPhotoToAdapter(bitmap);
        }
    }
    private void savePathToSharedPreferences() {
        Set<String> pathsSet;
        SharedPreferences pref = getSharedPreferences(PATH_FILE, MODE_PRIVATE);
        pathsSet = pref.getStringSet(PATH_KEY, null);
        if (pathsSet == null){
            pathsSet = new HashSet<>();
        }
        pathsSet.add(photoPath);
        pref.edit().clear().apply();
        pref.edit().putStringSet(PATH_KEY,pathsSet).apply();
    }
    private void loadBitmapImagesFromSharedPreferences(){
        bitmapsImages = new ArrayList<>();
        Set<String> pathsSet;
        SharedPreferences pref = getSharedPreferences(PATH_FILE, MODE_PRIVATE);
        pathsSet = pref.getStringSet(PATH_KEY, null);
        if (pathsSet == null){
            pathsSet = new HashSet<>();
        }
        for (String path : pathsSet) {
            bitmapsImages.add(new MyBitmap(createBitmap(path), path));
        }

    }
    private Bitmap createBitmap(String path) {
        int targetW = 396;
        int targetH = 436;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;
        int scaleFactor = Math.max(photoW/targetW, photoH/targetH);
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = getRoundedCornerBitmap(bitmap, 200);
        return bitmap;

    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, "jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;

    }
    private void addPhotoToAdapter(Bitmap bitmap){
        bitmapsImages.add(0,(new MyBitmap(bitmap, photoPath)));
        gridView.setAdapter(new ImageAdapter(this));
    }
    public class ListCardsAdapter extends PagerAdapter {


        private Context context;
        public ListCardsAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return layoutsRes.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater =
                    (LayoutInflater) container
                            .getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(layoutsRes.get(position), container, false);
            container.addView(view);
            if (layoutsRes.get(position) == R.layout.add_back_card_layout){

                gridView = findViewById(R.id.gridView);
                ImageAdapter imageAdapter = new ImageAdapter(context);
                gridView.setAdapter(imageAdapter);

            }


            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return bitmapsImages.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5,5,5,5);
            }else {
                imageView = (ImageView) convertView;
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String typeAndTag = PHOTO + DELIMITER + view.getTag();
                    setSettings(BACK_CARDS, typeAndTag);

                }
            });
            imageView.setImageBitmap(bitmapsImages.get(position).bitmap);
            imageView.setTag(bitmapsImages.get(position).pathBitmap);

            return imageView;
        }
    }
    class MyBitmap{
        public Bitmap bitmap;
        public String pathBitmap;

        public MyBitmap(Bitmap bitmap, String pathBitmap) {
            this.bitmap = bitmap;
            this.pathBitmap = pathBitmap;
        }

    }

}