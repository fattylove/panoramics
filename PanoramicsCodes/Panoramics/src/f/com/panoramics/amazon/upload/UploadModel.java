/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package f.com.panoramics.amazon.upload;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.PersistableUpload;
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.mobileconnectors.s3.transfermanager.exception.PauseException;

import f.com.panoramics.constant.Constant;
import f.com.panoramics.utils.AS3Util;

/* UploadModel handles the interaction between the Upload and TransferManager.
 * This also makes sure that the file that is uploaded has the same file extension
 *
 * One thing to note is that we always create a copy of the file we are given. This
 * is because we wanted to demonstrate pause/resume which is only possible with a
 * File parameter, but there is no reliable way to get a File from a Uri(mainly
 * because there is no guarantee that the Uri has an associated File).
 *
 * You can easily avoid this by directly using an InputStream instead of a Uri.
 */
public class UploadModel extends TransferModel {
    private static final String TAG = "UploadModel";

    private Upload mUpload;
    private PersistableUpload mPersistableUpload;
    private ProgressListener mListener;
    private Status mStatus;
    private Uri uri;

    public UploadModel(Context context, Uri uri, TransferManager manager) {
        super(context, uri, manager);
        this.uri = uri;
        
        System.err.println("UploadModel->" + uri.toString());
        
        mStatus = Status.IN_PROGRESS;
        mListener = new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent event) {
                if (event.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                    mStatus = Status.COMPLETED;
                    System.err.println("upload success ....");
                }
            }
        };
    }

    public Runnable getUploadRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                upload();
            }
        };
    }

    @Override
    public void abort() {
        if (mUpload != null) {
            mStatus = Status.CANCELED;
            mUpload.abort();
        }
    }

    @Override
    public Status getStatus() {
        return mStatus;
    }

    @Override
    public Transfer getTransfer() {
        return mUpload;
    }

    @Override
    public void pause() {
        if (mStatus == Status.IN_PROGRESS) {
            if (mUpload != null) {
                mStatus = Status.PAUSED;
                try {
                    mPersistableUpload = mUpload.pause();
                } catch (PauseException e) {
                    Log.d(TAG, "", e);
                }
            }
        }
    }

    @Override
    public void resume() {
        if (mStatus == Status.PAUSED) {
            mStatus = Status.IN_PROGRESS;
            if (mPersistableUpload != null) {
                mUpload = getTransferManager().resumeUpload(mPersistableUpload);
                mUpload.addProgressListener(mListener);
                mPersistableUpload = null;
            } else {
                upload();
            }
        }
    }

    public void upload() {
    	 try {
             mUpload = getTransferManager().upload(Constant.BUCKET_NAME.toLowerCase(Locale.US), AS3Util.getMediaPath(new File(uri.toString()).getName()),  new File(uri.toString()));
             mUpload.addProgressListener(mListener);
         } catch (Exception e) {
             Log.e(TAG, "", e);
         }
    }

}
