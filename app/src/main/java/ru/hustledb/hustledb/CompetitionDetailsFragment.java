package ru.hustledb.hustledb;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class CompetitionDetailsFragment extends Fragment {
    private static final String C_ID = "competitionId";
    private static final String C_URL = "competitionUrl";
    private static final String C_TITLE = "competitionTitle";
    private static final String C_DATE = "competitionDate";
    private static final String C_CITY = "competitionCity";
    private static final String C_DESC = "competitionDesc";

    private Competition competition;
    @BindView(R.id.detailTitle)
    TextView detailTitle;
    @BindView(R.id.detailCity)
    TextView detailCity;
    @BindView(R.id.detailDate)
    TextView detailDate;
    @BindView(R.id.detailDesc)
    TextView detailDesc;

    public CompetitionDetailsFragment() {
        // Required empty public constructor
    }

    public static CompetitionDetailsFragment newInstance(Competition competition) {
        CompetitionDetailsFragment fragment = new CompetitionDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(C_ID, competition.getId());
        args.putString(C_URL, competition.getUrl());
        args.putString(C_TITLE, competition.getTitle());
        args.putString(C_DATE, competition.getDate());
        args.putString(C_CITY, competition.getCity());
        args.putString(C_DESC, competition.getDesc());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!competition.getTitle().equals("")) {
            ((View)detailTitle.getParent()).setVisibility(View.VISIBLE);
            if(!competition.getUrl().equals("")){
                detailTitle.setMovementMethod(LinkMovementMethod.getInstance());
                String link = "<a href=" + competition.getUrl() + ">" +
                        competition.getTitle() + "</a>";
                detailTitle.setText(Html.fromHtml(link));
            } else {
                detailTitle.setText(competition.getTitle());
            }
        } else {
            ((View)detailTitle.getParent()).setVisibility(GONE);
        }
        if(!competition.getCity().equals("")){
            detailCity.setText(competition.getCity());
            ((View)detailCity.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View)detailCity.getParent()).setVisibility(GONE);
        }
        if(!competition.getDate().equals("")) {
            detailDate.setText(competition.getPrettyDate());
            ((View)detailDate.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View)detailDate.getParent()).setVisibility(GONE);
        }
        if(!competition.getDesc().equals("")) {
            detailDesc.setText(competition.getDesc());
            ((View)detailDesc.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View)detailDesc.getParent()).setVisibility(GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && competition == null) {
            Bundle bundle = getArguments();
            competition = new Competition(bundle.getInt(C_ID),
                    bundle.getString(C_URL),
                    bundle.getString(C_TITLE),
                    bundle.getString(C_DATE),
                    bundle.getString(C_DESC),
                    bundle.getString(C_CITY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }
}
