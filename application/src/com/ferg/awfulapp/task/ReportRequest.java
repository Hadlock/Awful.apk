package com.ferg.awfulapp.task;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.ferg.awfulapp.constants.Constants;
import com.ferg.awfulapp.provider.AwfulProvider;
import com.ferg.awfulapp.thread.AwfulThread;
import com.ferg.awfulapp.util.AwfulError;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by matt on 8/8/13.
 */
public class ReportRequest extends AwfulRequest<Void> {
    private int postId;
    private String mComments;
    public ReportRequest(Context context, int postId, String mComments) {
        super(context, null);
        this.mComments = mComments;
        this.postId = postId;
    }

    @Override
    protected String generateUrl(Uri.Builder urlBuilder) {
        addPostParam(Constants.PARAM_COMMENTS, mComments);
        addPostParam(Constants.PARAM_POST_ID, String.valueOf(postId));

        return Constants.FUNCTION_BOOKMARK;
    }

    @Override
    protected Void handleResponse(Document doc) throws AwfulError {

		Document result = doc;
		if(result.getElementById("content") != null){
			Element standard = result.getElementsByClass("standard").first();
			if(standard != null && standard.hasText()){
				if(standard.text().contains("Thank you, but this thread has already been reported recently!")){
					throw new AwfulError("Someone has already reported this thread recently");
					
				}else if(standard.text().contains("Your alert has been submitted to the Moderators.")){
					return null; //"Thank you for your report";
				}
			}
		}	
		return null;
    }

    @Override
    protected boolean handleError(AwfulError error, Document doc) {
        return error.isCritical();
    }
}
