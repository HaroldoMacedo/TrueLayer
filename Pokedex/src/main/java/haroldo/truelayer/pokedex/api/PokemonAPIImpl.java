package haroldo.truelayer.pokedex.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import haroldo.truelayer.pokedex.entity.PokemonInfo;

public class PokemonAPIImpl implements PokemonAPI {
	private static final Logger log = LoggerFactory.getLogger(PokemonAPIImpl.class);

	@Override
	public PokemonInfo getPokemonInfo(String pokemonName) {
		log.debug("Getting info of pokemon '{}'", pokemonName);
		return _getPokemonInfo(pokemonName);
	}

	@Override
	public PokemonInfo getPokemonTranslatedInfo(String pokemonName) {
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode;
		String translationUrl = "https://api.funtranslations.com/translate/";

		log.debug("Getting translated info of pokemon");

		// Get the pokemon information.
		PokemonInfo pokemon = _getPokemonInfo(pokemonName);

		// If error, returns.
		if (!pokemon.getError().isBlank())
			return pokemon;

		// Translate the pokemon description.
		if (pokemon.getHabitat().equalsIgnoreCase("cave") || pokemon.isLegendary()) {
			// Yoda translation.
			String translationJson = restTemplate
					.getForObject(translationUrl + "shakespeare.json?text=" + pokemon.getDescription(), String.class);
			try {
				rootNode = objectMapper.readTree(translationJson);
			} catch (JsonProcessingException e) {
				log.error("Pokemon '{}' found!", pokemonName);
				pokemon.setError("Error translating to Yoda");
				return pokemon;
			}
		} else {
			// Shakespeare translation.
			String translationJson = restTemplate
					.getForObject(translationUrl + "shakespeare.json?text=" + pokemon.getDescription(), String.class);
			try {
				rootNode = objectMapper.readTree(translationJson);
			} catch (JsonProcessingException e) {
				log.error("Pokemon '{}' was not found,", pokemonName);
				pokemon.setError("Error translating to Shakespeare");
				return pokemon;
			}
		}

		// Update the description with the translated text.
		pokemon.setDescription(rootNode.path("contents").path("translated").asText());

		return pokemon;
	}

	private PokemonInfo _getPokemonInfo(String pokemonName) {
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode;

		PokemonInfo pokemon = new PokemonInfo();
		pokemon.setName(pokemonName);

		log.debug("Pokemon name: '{}'", pokemonName);

		//
		// Get the pokemon information.
		String pokemonJson;
		try {
			pokemonJson = restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/" + pokemonName, String.class);
		} catch (HttpStatusCodeException e) {
			if (e.getRawStatusCode() == 404)
				pokemon.setError("Pokemon '" + pokemonName + "' not found!");
			else
				pokemon.setError(e.getMessage());
			log.error("Error: Pokemon " + pokemonName + ": " + e.getMessage());
			return pokemon;
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			pokemon.setError(e.getMessage());
			return pokemon;
		}

		try {
			rootNode = objectMapper.readTree(pokemonJson);
		} catch (JsonProcessingException e) {
			log.error("Pokemon '{}' was not found,", pokemonName);
			pokemon.setError("Pokemon not found");
			return pokemon;
		}

		//
		// Get the species information.
		String specieUrl = rootNode.path("species").path("url").asText();
		String specieJson = restTemplate.getForObject(specieUrl, String.class);
		try {
			rootNode = objectMapper.readTree(specieJson);
		} catch (JsonProcessingException e) {
			log.error("Specie of pokemon '{}' was not found. Used URL = '{}'", pokemonName, specieUrl);
			pokemon.setError("Specie of pokemon not found");
			return pokemon;
		}

		pokemon.setDescription(findDescriptionInEnglish(rootNode.path("flavor_text_entries")));
		pokemon.setHabitat(rootNode.path("habitat").path("name").asText());
		pokemon.setLegendary(rootNode.path("is_legendary").asBoolean());

		log.debug("Description: '{}'", pokemon.getDescription());
		log.debug("Habitat: '{}'", pokemon.getHabitat());
		log.debug("Is Legendary?: '{}'", (pokemon.isLegendary() ? "Yes" : "No"));

		return pokemon;
	}
	
	/**
	 * Find the firs description in english.
	 * @param flavors
	 * @return
	 */
	private String findDescriptionInEnglish(JsonNode flavors) {
		for (JsonNode node : flavors) {
			if (node.path("language").path("name").asText().equals("en"))
				return node.path("flavor_text").asText();
		}
		return "No description in english!";
	}

}
