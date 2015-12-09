package br.com.caelum.cadastro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.dao.AlunoDAO;

/**
 * Created by android5843 on 09/12/15.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] mensagens = (Object[]) bundle.get("pdus");
        byte[] mensagem = (byte[]) mensagens[0];

        SmsMessage sms = SmsMessage.createFromPdu(mensagem);

        AlunoDAO dao = new AlunoDAO(context);
        if (dao.isAluno(sms.getOriginatingAddress())) {
            Toast.makeText(context, "SMS de " + sms.getDisplayOriginatingAddress(), Toast.LENGTH_LONG).show();

            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();
    }
}
