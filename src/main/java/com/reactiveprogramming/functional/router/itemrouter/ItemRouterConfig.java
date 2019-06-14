package com.reactiveprogramming.functional.router.itemrouter;

import com.reactiveprogramming.functional.handler.itemhandler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.reactiveprogramming.controller.constants.ItemServiceConstants.ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ItemHandler itemHandler) {
        return RouterFunctions.route(GET(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON_UTF8)), itemHandler::getAllItems)
                .andRoute(GET(ITEM_CONTROLLER_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON_UTF8)), itemHandler :: getItemById);
    }

}
