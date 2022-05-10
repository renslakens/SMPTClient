package nl.avans.smtpstatemachine;

import nl.avans.SmtpContext;

public class WaitForRcptToOrDataState implements SmtpStateInf {
    SmtpContext context;

    public WaitForRcptToOrDataState(SmtpContext context) {
        this.context = context;
    }

    @Override
    public void Handle(String data) {
        //Handle "MAIL FROM: <user@domain.nl>" Command & TRANSITION TO NEXT STATE
        if(data.toUpperCase().startsWith("MAIL FROM: ")) {
            context.SetMailFrom(data.substring(11));
            context.SendData("250 Mail from " + context.GetMailFrom());
            context.SetNewState(new WaitForRcptToState(context));
            return;
        }
        //Handle "DATA" Command & TRANSITION TO NEXT STATE
        if(data.toUpperCase().startsWith("DATA ")) {
            context.AddTextToBody(data.substring(5));
            context.SetNewState(new ReceivingDataState(context));
            return;
        }
        //Handle "QUIT" Command
        if(data.toUpperCase().startsWith("QUIT")) {
            context.SendData("221 Bye");
            context.DisconnectSocket();
            return;
        }
        //Generate "503 Error on invalid input"
        context.SendData("503 Error...");
    }
}
