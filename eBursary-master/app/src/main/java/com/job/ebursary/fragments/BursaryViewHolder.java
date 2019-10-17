package com.job.ebursary.fragments;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.job.ebursary.R;
import com.job.ebursary.commoners.GetTimeAgo;
import com.job.ebursary.model.ApplicationModel;

import java.util.Date;


/**
 * Created by Job on Sunday : 6/24/2018.
 */
public class BursaryViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ApplicationModel model;
    private TextView admin_number;
    private TextView idnumber;
    private TextView county;
    private TextView amountrequest;
    private TextView name;
    private TextView address;
    private TextView timeago;
    private TextView school;


    public BursaryViewHolder(@NonNull View itemView) {
        super(itemView);

        admin_number = itemView.findViewById(R.id.admission);
        idnumber = itemView.findViewById(R.id.idnumber);
        county = itemView.findViewById(R.id.county);
        name = itemView.findViewById(R.id.name);
        school = itemView.findViewById(R.id.school);
        address = itemView.findViewById(R.id.address);
        timeago = itemView.findViewById(R.id.timeago);
        amountrequest = itemView.findViewById(R.id.amountrequest);

    }

    public void init(Context context, ApplicationModel model) {
        this.context = context;
        this.model = model;

        name.setText(model.getName());
        admin_number.setText(model.getAdmission());
        school.setText("   @ "+model.getSchool());
        amountrequest.setText("Student with ID Number: "+model.getIdnumber()+ " has applied for a bursary from the county of "+ model.getCounty() +
                " for amount totaling to Ksh: "+model.getAmountrequest() + "\n\n"+ "status: PENDING APPROVAL");
        address.setText(model.getAddress());

        Date dd = model.getTime().toDate();
        timeago.setText(GetTimeAgo.getTimeAgo(dd, context));
    }
}
