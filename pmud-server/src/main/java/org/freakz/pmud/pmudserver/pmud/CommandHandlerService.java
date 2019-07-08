package org.freakz.pmud.pmudserver.pmud;

import lombok.extern.slf4j.Slf4j;
import org.freakz.pmud.common.objects.PMudPlayer;
import org.freakz.pmud.pmudserver.pmud.handlers.AcceptVerbs;
import org.freakz.pmud.pmudserver.pmud.handlers.PMudVerbAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CommandHandlerService {

    @Autowired
    private ApplicationContext context;

    private Map<String, Handler> handlers = new HashMap<>();

    class Handler {
        Object clazz;
        Method method;
    }


    @PostConstruct
    public void resolveHandlers() {
        Map<String, Object> verbAcceptors = context.getBeansWithAnnotation(PMudVerbAcceptor.class);
        for (Object object : verbAcceptors.values()) {
            for (Method method : object.getClass().getMethods()) {
                if (method.isAnnotationPresent(AcceptVerbs.class)) {
                    AcceptVerbs annotation = method.getAnnotation(AcceptVerbs.class);
                    for (String verb : annotation.verbs()) {
                        Handler handler = new Handler();
                        handler.clazz = object;
                        handler.method = method;
                        handlers.put(verb, handler);
                    }
                }
            }
        }
    }

    public VerbResponse invokeVerb(String verb, PMudPlayer player) {
        VerbRequest request = new VerbRequest(verb, player);
        VerbResponse response = new VerbResponse(player);
        if (invokeVerbHandler(request, response)) {
            return response;
        }
        return null;
    }

    public boolean invokeVerbHandler(VerbRequest request, VerbResponse response) {
        Handler handler = handlers.get(request.getArgs().getCmd());
        if (handler != null) {
            try {
                handler.method.invoke(handler.clazz, request, response);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            for (String key : handlers.keySet()) {
                if (key.startsWith(request.getArgs().getCmd())) {
                    try {
                        handler = handlers.get(key);
                        handler.method.invoke(handler.clazz, request, response);
                        return true;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

}
