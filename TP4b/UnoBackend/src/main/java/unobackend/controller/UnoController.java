package unobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unobackend.service.UnoService;

import java.util.List;
import java.util.UUID;

@Controller
public class UnoController {

    @Autowired UnoService unoService;


//    // ejemplo de como funcionan los métodos del controler:
//
//    // corres UnoBackendApllication y entonces podes
//    // acceder a http://localhost:8080/hola en browser
      // (o via curl en terminal)
//
//    @GetMapping("/hola")
//    public ResponseEntity<String> holaMundo() {
//        return new ResponseEntity<>("Hola Mundo", HttpStatus.OK);
//    }

    // responde un uuid con la sesión asociada al juego creado
    @PostMapping("newmatch")
    public ResponseEntity newMatch(@RequestParam List<String> players) {
        return ResponseEntity.ok(unoService.newMatch(players));
    }
    // en terminal:
    // curl -X POST "http://localhost:8080/newmatch?players=Emilio&players=Julio" -H "Accept:application/json"
    // devuelve: "ee5a74b3-5fb3-4f5f-a4b3-95ae6fbbafe8"

    // TODO: hacer estos que faltan
//    @PostMapping("play/{matchId}/{player}")
//    public ResponseEntity play(@PathVariable UUID matchId, @PathVariable String player, @RequestBody JsonCard card) {
//
//    }
//    // curl -X POST "http://localhost:8080/play/6951e08e-3594-49ec-95a3-056382cea112/A" -H "Content-Type: application/json" -d '{"color":"Blue","number":6,"type":"NumberCard","shout":false}'
//
//    @PostMapping("draw/{matchId}/{player}")
//    public ResponseEntity drawCard(@PathVariable UUID matchId, @PathVariable String player) {
//
//    }

//    // responde una representación json de la carta a la vista
//    @PostMapping("activecard/{matchId}")
//    public ResponseEntity activeCard(@PathVariable UUID matchId) {
//
//    }

    // responde una representación json de la lista de cartas en la mano del jugador en turno
    @PostMapping("playerhand/{matchId}")
    public ResponseEntity playerHand(@PathVariable UUID matchId) {
        return ResponseEntity.ok( unoService.playerHand(matchId).stream().map(each -> each.asJson()));
    }
    // curl -X POST "http://localhost:8080/playerhand/de2f9be9-7687-46a0-99a3-f8bd74b23840" -H "Accept: application/json"
    // devuelve:
    // [{"color":"Green","number":6,"type":"NumberCard","shout":false},{"color":"Green","number":null,"type":"SkipCard","shout":false},{"color":"Blue","number":2,"type":"NumberCard","shout":false},{"color":"Blue","number":1,"type":"NumberCard","shout":false},{"color":"Green","number":null,"type":"ReverseCard","shout":false},{"color":"Green","number":2,"type":"NumberCard","shout":false},{"color":"Red","number":4,"type":"NumberCard","shout":false}]



}
