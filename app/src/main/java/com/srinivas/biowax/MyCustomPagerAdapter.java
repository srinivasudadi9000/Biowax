package com.srinivas.biowax;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyCustomPagerAdapter extends PagerAdapter{
    Context context;
    int images[];
    LayoutInflater layoutInflater;
    String des[] = {"Bio Waste Disposal having some advantages It will helps to save environment",
            "Bio Waste Disposal having some advantages It will helps to save environment and this is maintained by schemax",
            "Bio Waste Disposal having some advantages It will helps to save environment .some disposable items can't convert to mould",
            "Bio Waste Disposal material wastage,bulk injections ,cotton material etc.,",
            "Disposalble Items can take place and preserve and disposable to maintained temperature .it will save environament",
            "Disposalble Items can take place and preserve and disposable to maintained temperature .it will save environament"};

    public MyCustomPagerAdapter(Context context, int images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item, container, false);
        TextView textView = (TextView) itemView.findViewById(R.id.description_tv);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);
        textView.setText(des[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
