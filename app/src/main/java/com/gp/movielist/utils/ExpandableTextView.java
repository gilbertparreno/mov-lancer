package com.gp.movielist.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gp.movielist.R;

/**
 * Created by gilbert on 12/17/18.
 */
public class ExpandableTextView extends TextView {

    private static final int TRUNCATE_AT = 199;

    private boolean isExpandable = false;
    private String originalText;
    private int truncateAt;
    private boolean expanded = false;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a =
                context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, 0, 0);
        try {
            isExpandable = a.getBoolean(R.styleable.ExpandableTextView_enableExpand, false);
            truncateAt = a.getInteger(R.styleable.ExpandableTextView_truncateAt, TRUNCATE_AT);
        } finally {
            a.recycle();
        }
    }

    public void setText(String text) {
        if (isExpandable && text.length() >= truncateAt) {
            originalText = text;
            String t;

            if (!expanded) {
                t = text.substring(0, truncateAt - 1) + "...  [See more]";
            } else {
                t = text + "\n[See less]";
            }
            ClickableSpan clickableSpan =
                    new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            expanded = !expanded;
                            setText(originalText);
                        }
                    };
            SpannableString spannableString = new SpannableString(t);
            spannableString.setSpan(
                    clickableSpan, t.length() - 10, t.length(), 0);
            setText(spannableString, TextView.BufferType.SPANNABLE);
            setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            super.setText(text);
        }
    }
}
