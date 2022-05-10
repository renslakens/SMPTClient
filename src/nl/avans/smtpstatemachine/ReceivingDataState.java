package nl.avans.smtpstatemachine;

import nl.avans.SmtpContext;

public class ReceivingDataState implements SmtpStateInf {
    SmtpContext context;
    boolean crReceived;
    boolean dotReceived;

    public ReceivingDataState(SmtpContext context) {
        this.context = context;
    }

    @Override
    public void Handle(String data) {
        //handle the receiving of the mailbody
        if(data.equals("\\")) {
            crReceived = true;
        }
        if(data.equals(".")) {
            dotReceived = true;
        }
        if(dotReceived == true) {
            context.SendData("Message accepted for delivery");
            //"\r\n.\r\n" should return to the WaitForMailFromState
            context.SetNewState(new WaitForMailFromState(context));
            return;
        }

    }
}
