package com.personal.rents.task;

import java.io.File;

import com.personal.rents.task.listener.OnCreateImageFileTaskFinishListener;
import com.personal.rents.util.MediaUtils;

import android.content.Context;
import android.os.AsyncTask;

public class CreateImageFileAsyncTask extends AsyncTask<Context, Void, File>{
	
	OnCreateImageFileTaskFinishListener onCreateImageFileTaskFinishListener;
	
	public CreateImageFileAsyncTask(OnCreateImageFileTaskFinishListener 
			onCreateImageFileTaskFinishListener) {
		this.onCreateImageFileTaskFinishListener = onCreateImageFileTaskFinishListener;
	}

	@Override
	protected File doInBackground(Context... params) {
		Context context = params[0];
		
		File imageFile = MediaUtils.createImageFile(context);

		return imageFile;
	}

	@Override
	protected void onPostExecute(File result) {
		onCreateImageFileTaskFinishListener.onTaskFinish(result);
	}

}
