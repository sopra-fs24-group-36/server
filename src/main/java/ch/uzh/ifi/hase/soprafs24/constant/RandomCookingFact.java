package ch.uzh.ifi.hase.soprafs24.constant;

public enum RandomCookingFact {
  FACT_1("Breakfast is NOT the most important meal of the day. In 1944, General Foods coined the phrase to sell more cereal Grape Nuts."),
  FACT_2("Pound cake got its name from its recipe which called for one pound of butter, one pound of eggs, and one pound of sugar."),
  FACT_3("Most supermarket wasabi is actually colored horseradish with flavorings."),
  FACT_4("The most expensive pizza in the world costs $12,000 and includes luxurious ingredients like three types of caviar and lobster."),
  FACT_5("Ranch dressing is dyed with titanium dioxide to make it look whiter."),
  FACT_6("One fast-food burger can have meat from 100 different cows."),
  FACT_7("Fruit-flavored snacks shine because of carnauba wax, the same type used on cars."),
  FACT_8("Nutmeg acts as a hallucinogen if ingested in large doses."),
  FACT_9("Ketchup was once used as a medicine in the early 1800s."),
  FACT_10("White chocolate isn't technically chocolate as it contains no cocoa solids."),
  FACT_11("Farmed salmon is dyed pink to mimic the natural pink color of wild salmon."),
  FACT_12("The red food dye for Skittles is made from boiled beetles."),
  FACT_13("Crackers are worse for your teeth than sugar because they stick to the teeth and promote bacteria growth."),
  FACT_14("Peppers do not actually burn your mouth; the sensation is caused by capsaicin."),
  FACT_15("American cheese was actually invented in Switzerland."),
  FACT_16("Until 2013, Russia did not classify beer as an alcoholic beverage."),
  FACT_17("Cheese is the most stolen food in the world."),
  FACT_18("One in four hazelnuts ends up in Nutella."),
  FACT_19("A corned beef sandwich was smuggled into space by an astronaut."),
  FACT_20("Loud music can make people drink faster according to research."),
  FACT_21("Adding salt to water increases its boiling point."),
  FACT_22("Olive oil stops pasta from sticking together."),
  FACT_23("Apples are more effective at waking you up in the morning than coffee."),
  FACT_24("Honey never spoils."),
  FACT_25("Cranberries bounce when they are ripe."),
  FACT_26("A corned beef sandwich was smuggled into space."),
  FACT_27("Certain music can make you drink faster."),
  FACT_28("Chocolate has been used as a currency in ancient civilizations of Mexico and South America."),
  FACT_29("McDonald's sells 2.5 billion hamburgers every year."),
  FACT_30("Peanuts are used in the production of dynamite."),
  FACT_31("Expiration dates on bottled water have nothing to do with the water itself."),
  FACT_32("Arachibutyrophobia is the fear of peanut butter sticking to the roof of your mouth."),
  FACT_33("Spam was not invented in Hawaii."),
  FACT_34("Honey is considered bee vomit."),
  FACT_35("The original Three Musketeers candy bar had three flavors."),
  FACT_36("Froot Loops are all the same flavor."),
  FACT_37("In ancient Egypt, radishes, onions, and garlic were used as wages."),
  FACT_38("Pufferfish must be prepared by specially trained chefs in Japan."),
  FACT_39("French fries originated in Belgium."),
  FACT_40("Twinkie cream is not cream but vegetable shortening."),
  FACT_41("Strawberries are not actually berries."),
  FACT_42("Pineapples have no relation to pine trees."),
  FACT_43("No one knows the true origin of chocolate chip cookies."),
  FACT_44("Margherita pizza is named after a queen."),
  FACT_45("Thomas Jefferson made pasta popular in the U.S."),
  FACT_46("Cauliflower can be purple, orange, or green."),
  FACT_47("Lima beans contain lethal amounts of cyanide when raw."),
  FACT_48("Chickpeas have more names and colors than you might think."),
  FACT_49("Not all popcorn served in movie theaters around the world is the same."),
  FACT_50("Not all wine is vegan due to common fining agents used."),
  FACT_51("Goat meat is the most popular meat globally, accounting for 70% of the red meat eaten."),
  FACT_52("Throwing food away is illegal in Seattle; businesses face fines for non-compliance."),
  FACT_53("Norman Borlaug developed crop strains that greatly increased food yields, saving over a billion lives."),
  FACT_54("California is the world's 5th largest supplier of food."),
  FACT_55("Astronauts ate food grown in space for the first time in 2015."),
  FACT_56("Some produce in the U.S. is considered too ugly to sell."),
  FACT_57("Sound can influence the taste of your food; high frequencies enhance sweetness."),
  FACT_58("Death row inmates in Texas no longer get to pick their last meal."),
  FACT_59("Australians eat the most meat per person annually."),
  FACT_60("Americans consume enough peanut butter annually to coat the floor of the Grand Canyon."),
  FACT_61("There are more Indian restaurants in London than in Mumbai or Delhi."),
  FACT_62("The Columbian Exchange drastically changed the global spread of plants and seeds."),
  FACT_63("Hot chocolate tastes better from an orange cup, according to a study."),
  FACT_64("The original Margherita pizza was designed to represent the Italian flag."),
  FACT_65("Not eating before bed can increase the amount of fat your body burns while sleeping."),
  FACT_66("It is impossible to cook an egg on a sidewalk, even on the hottest day."),
  FACT_67("The Domino’s co-founder traded his shares for a Volkswagen Beetle."),
  FACT_68("Breakfast was promoted as the most important meal of the day as a marketing strategy by General Foods in 1944."),
  FACT_69("Eskimos use refrigerators to prevent their food from freezing."),
  FACT_70("Eating fast food regularly can have similar impacts on your liver as hepatitis."),
  FACT_71("A ridiculous amount of Nutella is sold every year, enough to cover The Great Wall of China 8 times."),
  FACT_72("German chocolate cake has nothing to do with Germany; it was named after an American baker, Sam German."),
  FACT_73("Tonic water glows under UV light due to the quinine it contains."),
  FACT_74("Coriander and cilantro refer to different parts of the same plant."),
  FACT_75("Alabama's state nut is the pecan, celebrated with an annual pecan festival.");

  private final String fact;

  RandomCookingFact(String fact) {
    this.fact = fact;
  }

  public String getFact() {
    return this.fact;
  }

  public static String getRandomFact() {
    RandomCookingFact[] facts = values();
    int index = (int) (Math.random() * facts.length);
    return facts[index].getFact();
  }
}