package com.messieyawo.com.ispacejobintentservice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.core.app.JobIntentService

import com.messieyawo.com.ispacejobintentservice.Constants.EXTRA_FILE_URL
import com.messieyawo.com.ispacejobintentservice.Constants.FILE_CONTENTS_KEY
import com.messieyawo.com.ispacejobintentservice.Constants.JOB_ACTION
import com.messieyawo.com.ispacejobintentservice.Constants.JOB_ID
import com.messieyawo.com.ispacejobintentservice.Constants.LOG_TAG
import com.messieyawo.com.ispacejobintentservice.Constants.RECEIVER_KEY
import java.net.URL
import java.nio.charset.Charset


class MyIntentService : JobIntentService() {


    override fun onHandleWork(intent: Intent) {
        if (intent.action == JOB_ACTION) {
            val url = URL(intent.getStringExtra(EXTRA_FILE_URL))
            val contents = url.readText(Charset.defaultCharset())
            Log.i(LOG_TAG, contents)

            val bundle = Bundle()
            bundle.putString(FILE_CONTENTS_KEY, contents)
            val receiver = intent.getParcelableExtra<ResultReceiver>(RECEIVER_KEY)
            receiver?.send(Activity.RESULT_OK, bundle)
        }
    }



    companion object {
        fun startAction(context: Context, fileUrl: String, receiver: ResultReceiver) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = JOB_ACTION
                putExtra(RECEIVER_KEY, receiver)
                putExtra(EXTRA_FILE_URL, fileUrl)
            }
            enqueueWork(context, MyIntentService::class.java, JOB_ID, intent)
        }
    }
}