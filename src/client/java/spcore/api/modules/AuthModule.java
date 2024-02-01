package spcore.api.modules;


import spcore.api.AuthContext;
import spcore.api.SpCoreApi;
import spcore.api.SpCoreModule;
import spcore.api.delegates.Action;

public class AuthModule extends SpCoreModule {

    public void IsAuthorized(Action success, Action error) {
        var request = CreatePostRequest(b ->{
            b.setPath("/api/v1/crypto/isAuthByToken");
        }, NoneBody);

        Send(client.newCall(request.build()), (c, r) -> {
            if(!r.isSuccessful()){
                AuthContext.Logout();
                error.invoke();
                return;
            }
            AuthContext.setIsAuthorized(true);
            AuthContext.SaveSecret();
            success.invoke();
        });
    }
}
