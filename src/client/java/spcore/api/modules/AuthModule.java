package spcore.api.modules;


import spcore.api.AuthContext;
import spcore.api.SpCoreModule;
import spcore.api.delegates.Action;
import spcore.fabric.spcore.interfaces.ApiCallback;

public class AuthModule extends SpCoreModule {

    public void IsAuthorized(Action success, Action error) {
        var request = CreateGetRequest(b ->{
            b.setPath("/api/v1/crypto/isAuthByToken");
        });

        Send(client.newCall(request.build()), (c, r) -> {
            if(!r.isSuccessful()){
                AuthContext.Logout();
                error.invoke();
            }
            AuthContext.setIsAuthorized(true);
            AuthContext.SaveSecret();
        });
    }
}
