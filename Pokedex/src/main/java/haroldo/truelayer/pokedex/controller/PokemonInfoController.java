package haroldo.truelayer.pokedex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import haroldo.truelayer.pokedex.api.PokemonAPI;
import haroldo.truelayer.pokedex.entity.PokemonInfo;

/**
 * Class that receives the HTTP requests and respond with the Pokemon information.
 * 
 * @author Haroldo
 *
 */
@RestController
public class PokemonInfoController {
	private static final Logger log = LoggerFactory.getLogger(PokemonInfoController.class);
	private PokemonAPI pokemonAPI;

	@Autowired
	public PokemonInfoController(PokemonAPI pokemonAPI) {
		this.pokemonAPI = pokemonAPI;
	}

	@GetMapping(path = "/pokemon/{pokemonName}")
	public ResponseEntity<PokemonInfo> getPokemonInfo(@PathVariable String pokemonName) {
		log.debug("Estou em '/pokemon/{}'", pokemonName);
		PokemonInfo pokemon = pokemonAPI.getPokemonInfo(pokemonName);
		return ResponseEntity.accepted().body(pokemon);
	}

	@GetMapping(path = "/pokemon/translated/{pokemonName}")
	public ResponseEntity<PokemonInfo> getPokemonTranslatedInfo(@PathVariable String pokemonName) {
		log.debug("Estou em '/pokemon/translated/{}'", pokemonName);
		PokemonInfo pokemon = pokemonAPI.getPokemonTranslatedInfo(pokemonName);
		return ResponseEntity.accepted().body(pokemon);
	}
}
