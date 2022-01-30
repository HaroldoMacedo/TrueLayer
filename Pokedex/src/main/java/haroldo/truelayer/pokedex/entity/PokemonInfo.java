package haroldo.truelayer.pokedex.entity;

/**
 * Defines the information that will be returned by this API on the HTTP response.
 * 
 * @author Haroldo
 *
 */
public class PokemonInfo {

	private String error = "";	//	If error is "", then no error has occurred.
	private String name = "";
	private String description = "";
	private String habitat = "";
	private boolean isLegendary = false;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.replaceAll("[\\\t|\\\n|\\\r|\\f]"," ");
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public boolean isLegendary() {
		return isLegendary;
	}

	public void setLegendary(boolean isLegendary) {
		this.isLegendary = isLegendary;
	}
	
	@Override
	public String toString() {
		return "Name: '" + name + "', Habitat: '" + habitat + "', Is Legendary? '" + (isLegendary ? "Yes" : "No") + "' Description: '" + description + "'"; 
	}

}
