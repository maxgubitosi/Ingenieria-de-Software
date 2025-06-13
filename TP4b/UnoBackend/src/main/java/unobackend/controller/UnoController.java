package unobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import unobackend.model.JsonCard;
import unobackend.service.UnoService;

import java.util.List;
import java.util.UUID;

@Controller
public class UnoController {

    @Autowired UnoService unoService;

    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<String> handleIllegal(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body( "Error: " + exception.getMessage() );
    }

    @ExceptionHandler(RuntimeException.class) public ResponseEntity<String> handleRuntime(RuntimeException exception) {
        return ResponseEntity.badRequest().body( "Runtime Error: " + exception.getMessage() );
    }

    // Esto para agarrar los malos jsons que les pegan a .asCard() en JsonCard en principio
    // TODO: quizas tiene sentido crear un BadJsonException o algo asi
    @ExceptionHandler({ClassNotFoundException.class})
    public ResponseEntity<String> handleReflectionExceptions(Exception exception) {
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "JSON parse error: " + exception.getMessage() );
    }





    @PostMapping("newmatch")
    public ResponseEntity newMatch(@RequestParam List<String> players) {
        return ResponseEntity.ok(unoService.newMatch(players));
    }
    // curl -X POST "http://localhost:8080/newmatch?players=Emilio&players=Julio" -H "Accept:application/json"

    @PostMapping("play/{matchId}/{player}")
    public ResponseEntity play(@PathVariable UUID matchId,
                               @PathVariable String player,
                               @RequestBody JsonCard card) {
        unoService.playerPlaysCard(matchId,player,card);
        return ResponseEntity.ok("");
    }
    // curl -X POST "http://localhost:8080/play/UUID/Emilio" -H "Content-Type: application/json" -d '{"color":"Blue","number":6,"type":"NumberCard","shout":false}'//

    @PostMapping("draw/{matchId}/{player}")
    public ResponseEntity drawCard(@PathVariable UUID matchId, @PathVariable String player) {
        unoService.playerDrawsCard(matchId, player);
        return ResponseEntity.ok("");
    }
    // curl -X POST "http://localhost:8080/drawCard/UUID/Emilio" -H "Accept: application/json"

    @PostMapping("activecard/{matchId}")
    public ResponseEntity activeCard(@PathVariable UUID matchId) {
        return ResponseEntity.ok(unoService.activeCard(matchId).asJson());
    }
    // curl -X POST "http://localhost:8080/activecard/UUID" -H "Accept: application/json"

    @PostMapping("playerhand/{matchId}")
    public ResponseEntity playerHand(@PathVariable UUID matchId) {
        // TODO: ver como devolver Error si recibe un matchId que no existe
        return ResponseEntity.ok(unoService.playerHand(matchId).stream().map(each -> each.asJson()));
    }
    // curl -X POST "http://localhost:8080/playerhand/UUID" -H "Accept: application/json"
}
