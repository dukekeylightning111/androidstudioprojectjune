package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SportingActivityAdapter extends RecyclerView.Adapter<SportingActivityAdapter.ViewHolder> {
    private List<SportingActivityClass> sportingActivities = new ArrayList<>();
    private LiveData<SportingActivityClass> liveSportingActivities;
    private Customer customer;
    private SportingActivityClassDao sportingActivityClassDao;
    private Context context;
    private DoubleClickListener doubleClickListener;

    public void deleteItem(int position) {
        sportingActivities.remove(position);
        notifyItemRemoved(position);
    }

    public SportingActivityAdapter(Customer customer, SportingActivityClassDao sportingActivityClassDao, Activity activity, Context context) {
        this.customer = customer;
        this.sportingActivityClassDao = sportingActivityClassDao;
        this.context = context;
        new LoadSportingActivitiesTask().execute();
        doubleClickListener = new DoubleClickListener(new DoubleClickListener.OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View view) {
                int position = (int) view.getTag();
                SportingActivityClass sportingActivity = getSportingActivityAtPosition(position);

                String message = customer.getName().toString() + "הזמין אותכם!" 
                       + "פעולת ספורט: "  + sportingActivity.getTitle()
                        + "\nזמן: " + sportingActivity.getTime()
                        + "\nקטגוריה" + sportingActivity.getTitle()
                        + "\nמיקום: " + sportingActivity.getLocation()
                        + "\nתיאור: " + sportingActivity.getDescription();

                showConfirmationDialog(message);
            }
        });
    }
    private void showConfirmationDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("אשרו בחירת פעולה לפני השיתוף")
                .setMessage("אתם מאשרים שיתוף לפעולה זו?" + message)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         showShareDialog(message);
                    }
                })
                .setNegativeButton("ביטול", null)
                .show();
    }
    private void showShareDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("שתפו פעולה")
                .setMessage("איך תרצו לשתף פעולה זו?")
                .setPositiveButton("הודעת SMS?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendSMS(message);
                    }
                })
                /*.setNegativeButton("הודעת דואר אלקטרוני?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendEmail(message);
                    }
                })
                .setNeutralButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendWhatsApp(message);
                    }
                })public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private Button shareButton;
    private TextView timeTextView;
    private TextView locationTextView;
    private TextView adminCustomerTextView;
    private TextView activityDescTextView;
    private ImageView imageViewCategory;
    private Context context;

    public ViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.sport_activity_name);
        timeTextView = itemView.findViewById(R.id.sport_activity_time);
        locationTextView = itemView.findViewById(R.id.sport_activity_location);
        activityDescTextView = itemView.findViewById(R.id.act_desc);
        imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
        context = itemView.getContext();
    }

    public void bind(@NonNull SportingActivityClass sportingActivity) {
        if (sportingActivity != null) {
            String name = sportingActivity.getTitle();
            String time = sportingActivity.getTime();
            String location = sportingActivity.getLocation();
            locationTextView.setText("מיקום הפעןולה" + location);
            String description = sportingActivity.getDescription();
            Customer customer = sportingActivity.getCustomer();

            nameTextView.setText(name);
            timeTextView.setText(sportingActivity.getDate() + " " + time);
            activityDescTextView.setText(description);
            imageViewCategory.setScaleType(ImageView.ScaleType.CENTER_CROP);

            String category = sportingActivity.getCategory();
            if (category.equals("football")) {
                imageViewCategory.setImageResource(R.drawable.people_football_image);
            } else if (category.equals("basketball")) {
                imageViewCategory.setImageResource(R.drawable.basketbal_in_activity);
            } else if (category.equals("tennis")) {
                imageViewCategory.setImageResource(R.drawable.tennis_in_activity_wallpaper);
            } else if (category.equals("volleyball")) {
                imageViewCategory.setImageResource(R.drawable.people_volleyval_image);
            }

            itemView.setTag(getAdapterPosition()); // Set the tag here
        } else{
            nameTextView.setText("לא יצרתם פעולות ספורט");
        }
    }
}

                */.show();
    }
    private void sendSMS(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private void sendEmail(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "אתם הוזמנתם לפעולת הפורט שלי! הנה פרטי פעולת הספורט");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
    private void sendWhatsApp(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "אין גישה לאפליצית WhatsApp במכשיר זה", Toast.LENGTH_SHORT).show();
        }
    }

    public SportingActivityAdapter(List<SportingActivityClass> sportingActivities, Customer customer) {
        this.customer = customer;
        this.sportingActivities = sportingActivities;
    }

    public void setSportingActivities(List<SportingActivityClass> sportingActivities) {
        this.sportingActivities.clear();

        if (sportingActivities != null) {
            this.sportingActivities.addAll(sportingActivities);
        }

        notifyDataSetChanged();
    }

    public SportingActivityClass getSportingActivityAtPosition(int position) {
        if (position >= 0 && position < sportingActivities.size()) {
            return sportingActivities.get(position);
        } else {
            return null;
        }
    }

    public void removeSportingActivity(int position) {
        sportingActivities.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteSportingActivity(SportingActivityClass sportingActivityClass, SportingActivityClassDao sportingActivityClassDao) {
        sportingActivityClassDao.delete(sportingActivityClass);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sporting_activity_item, parent, false);
        view.setOnClickListener(doubleClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sportingActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return sportingActivities.size();
    }

    private class LoadSportingActivitiesTask extends AsyncTask<Void, Void, List<SportingActivityClass>> {
        @Override
        protected List<SportingActivityClass> doInBackground(Void... voids) {
            return sportingActivityClassDao.getSportingActivitiesByUserId(customer.id);
        }

        @Override
        protected void onPostExecute(List<SportingActivityClass> sportingActivities) {
            super.onPostExecute(sportingActivities);
            setSportingActivities(sportingActivities);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Button shareButton;
        private TextView timeTextView;
        private TextView locationTextView;
        private TextView adminCustomerTextView;
        private TextView activityDescTextView;
        private ImageView imageViewCategory;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sport_activity_name);
            timeTextView = itemView.findViewById(R.id.sport_activity_time);
            locationTextView = itemView.findViewById(R.id.sport_activity_location);
            activityDescTextView = itemView.findViewById(R.id.act_desc);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
            context = itemView.getContext();
        }

        public void bind(@NonNull SportingActivityClass sportingActivity) {
            if (sportingActivity != null) {
                String name = sportingActivity.getTitle();
                String time = sportingActivity.getTime();
                String location = sportingActivity.getLocation();
                locationTextView.setText("מיקום הפעןולה" + location);
                String description = sportingActivity.getDescription();
                Customer customer = sportingActivity.getCustomer();

                nameTextView.setText(name);
                timeTextView.setText(sportingActivity.getDate() + " " + time);
                activityDescTextView.setText(description);
                imageViewCategory.setScaleType(ImageView.ScaleType.CENTER_CROP);

                String category = sportingActivity.getCategory();
                if (category.equals("football")) {
                    imageViewCategory.setImageResource(R.drawable.people_football_image);
                } else if (category.equals("basketball")) {
                    imageViewCategory.setImageResource(R.drawable.basketbal_in_activity);
                } else if (category.equals("tennis")) {
                    imageViewCategory.setImageResource(R.drawable.tennis_in_activity_wallpaper);
                } else if (category.equals("volleyball")) {
                    imageViewCategory.setImageResource(R.drawable.people_volleyval_image);
                }

                itemView.setTag(getAdapterPosition()); // Set the tag here
            } else{
                nameTextView.setText("לא יצרתם פעולות ספורט");
            }
        }
    }
}
/*
    public SportingActivityAdapter(LiveData<SportingActivityClass> liveSportingActivities, Customer customer, Activity activity) {
        this.liveSportingActivities = liveSportingActivities;
        this.customer = customer;
        this.context = activity;
    }
*/
