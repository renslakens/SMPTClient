package nl.avans.smtpstatemachine;

import nl.avans.SmtpContext;

public class WaitForRcptToState implements SmtpStateInf {
    SmtpContext context;

    public WaitForRcptToState(SmtpContext context) {
        this.context=context;
    }

    @Override
    public void Handle(String data) {
        //Handle "RCPT TO: <user@domain.nl>" Command & TRANSITION TO NEXT STATE
        if(data.toUpperCase().startsWith("RCPT TO: ")) {
            context.AddRecipient(data.substring(9));
            context.SendData("Recipient OK");
            context.SetNewState(new WaitForRcptToOrDataState(context));
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
