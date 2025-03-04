package be.kdg.backendjava.configuration;

import be.kdg.backendjava.domain.*;
import be.kdg.backendjava.repositories.*;
import org.hibernate.annotations.Check;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class Seedservice {
        private final CheckpointRepository checkpointRepository;
        private final TagRepository tagRepository;
        private final LocationRepository locationRepository;
        private final ProjectRepository projectRepository;
        private final UserRepository userRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final CommentRepository commentRepository;
        private final LikeRepository likeRepository;

        private List<Checkpoint> allCheckpoints = new ArrayList<>();
        private List<Tag> alltags = new ArrayList<>();
        private List<Location> allLocations = new ArrayList<>();


        public Seedservice(CheckpointRepository checkpointRepository, TagRepository tagRepository, LocationRepository locationRepository, ProjectRepository projectRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CommentRepository commentRepository, LikeRepository likeRepository) {
                this.checkpointRepository = checkpointRepository;
                this.tagRepository = tagRepository;
                this.locationRepository = locationRepository;
                this.projectRepository = projectRepository;
                this.userRepository = userRepository;
                this.bCryptPasswordEncoder = bCryptPasswordEncoder;
                this.commentRepository = commentRepository;
                this.likeRepository = likeRepository;
        }

        @EventListener()
        public void seedDatabase(ContextRefreshedEvent event) {
                //users
                final String encryptedPassword = bCryptPasswordEncoder.encode("admin");
                User user = new User(1L, "admin", "admin", "admin", "", UserRole.ADMIN, false, true);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                final String encryptedPassword2 = bCryptPasswordEncoder.encode("user");
                User user2 = new User(2L, "user", "user", "user", "", UserRole.USER, false, true);
                user2.setPassword(encryptedPassword2);
                userRepository.save(user2);

                //PROJECT AANMAKEN EN SAVE
                Project historischCentrumAntwerpen = new Project("historisch antwerpen",
                        "Historisch centrum Antwerpen, verschillende momnumenten en bezienswaardigheden in het gehele grondgebied Antwerpen");
                Project meerjarenPlanOpera = new Project("meerjarenplan Opera plein",
                        "Het Opera-plein wordt vernieuwd, van een fontein tot nieuwe banken");
                Project gastronomischAntwerpen = new Project("gastronomisch antwerpen",
                        "Van een klassiek café tot een 5 sterren restaurant, geniet van een gastronomisch uitstapje in Antwerpen");
                projectRepository.save(historischCentrumAntwerpen);
                projectRepository.save(meerjarenPlanOpera);
                projectRepository.save(gastronomischAntwerpen);

                //LOCATIE AANMAKEN EN SAVE
                Location historischLocatie1 = new Location(51.220442,4.400552); //kathedraal
                Location historischLocatie2 = new Location(51.217435, 4.420977); //Antwerpen-centraal
                Location historischLocatie3 = new Location(51.222520, 4.397592); //het steen
                Location historischLocatie4 = new Location(51.228257, 4.404944); //het mas
                allLocations.add(historischLocatie1);
                allLocations.add(historischLocatie2);
                allLocations.add(historischLocatie3);
                allLocations.add(historischLocatie4);

                Location meerjarenPlanLocatie1 = new Location(51.217985, 4.415400); //paneel algemeen
                Location meerjarenPlanLocatie2 = new Location(51.218397, 4.415576); //achter opera (fontein)
                Location meerjarenPlanLocatie3 = new Location(51.218888, 4.416100); //achter opera (bank)
                allLocations.add(meerjarenPlanLocatie1);
                allLocations.add(meerjarenPlanLocatie2);
                allLocations.add(meerjarenPlanLocatie3);

                Location gastronomischLocatie1 = new Location(51.219943, 4.400743); //da giovanni
                Location gastronomischLocatie2 = new Location(51.219875, 4.401000); //ramen noodles
                Location gastronomischLocatie3 = new Location(51.217784, 4.418352); //bier centraal
                Location gastronomischLocatie4 = new Location(51.217611, 4.409220); //The bistro
                allLocations.add(gastronomischLocatie1);
                allLocations.add(gastronomischLocatie2);
                allLocations.add(gastronomischLocatie3);
                allLocations.add(gastronomischLocatie4);

                /*Location test3Dvuilbak = new Location(51.218599, 4.400902);//start kdg
                Location test3Dboom = new Location(51.217832, 4.400363);//eind kdg
                Location test3Dfontain = new Location(51.218946, 4.401883);//rechts groenplaats
                Location test3Dbench = new Location(51.219666, 4.401358);//links boven groenplaats
                allLocations.add(test3Dvuilbak);
                allLocations.add(test3Dboom);
                allLocations.add(test3Dfontain);
                allLocations.add(test3Dbench);*/

                for (Location l:allLocations) {
                        locationRepository.save(l);
                }

                //CHECKPOINTS AANMAKEN EN SAVE
                Checkpoint historischCheckpoint1 = new Checkpoint("Kathedraal",
                        "De Onze-Lieve-Vrouwekathedraal is een kathedraal in de Belgische stad Antwerpen. De kathedraal staat aan de " +
                                "Handschoenmarkt en is de hoofdkerk van het bisdom Antwerpen. Ze is gewijd aan Maria. De kerk was een kathedraal " +
                                "tussen 1559 en 1803 en vanaf 1961 tot heden.",
                        "De prachtige kathedraal van Antwerpen steekt boven het centrum van de stad uit door de grote toren van 123 meter hoog. " +
                                "De kathedraal is meerdere malen gebouwd, afgebroken en weer opgebouwd waardoor je er verschillende architectonische " +
                                "stijlen kan vinden.",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Cathedral_of_Our_Lady_%28Antwerp%29_1800.jpg/260px-Cathedral_of_Our_Lady_%28Antwerp%29_1800.jpg",
                        historischLocatie1, historischCentrumAntwerpen);
                Checkpoint historischCheckpoint2 = new Checkpoint("Antwerpen-Centraal station",
                        "Station Antwerpen-Centraal (door de Antwerpenaren ook wel Centraal Station, Middenstatie of Spoorwegkathedraal genoemd, " +
                                "en algemeen ook ingekort tot Antwerpen-Centraal) is het centraal station aan het Koningin Astridplein in Antwerpen. " +
                                "Van 1873 tot begin 2007 was het een kopstation waar alle treinen hun rijrichting moesten keren. Op 23 maart 2007 werd " +
                                "onder een deel van de stad en onder het station een tunnel met twee doorgaande sporen geopend.",
                        "Station Antwerpen-Centraal is het centraal station aan het Koningin Astridplein in Antwerpen. Van 1873 tot begin 2007 was " +
                                "het een kopstation waar alle treinen hun rijrichting moesten keren.",
                        "https://www.architectura.be/img-poster/Antwerpen%20Centraal%20voorkant%2019e%20eeuw.jpg",
                        historischLocatie2, historischCentrumAntwerpen);
                Checkpoint historischCheckpoint3 = new Checkpoint("Het steen",
                        "De middeleeuwse burcht deed honderden jaren dienst als gevangenis, maar sinds de 19de eeuw is het een museum. " +
                                "Bij de burcht kan je ook een standbeeld vinden van Lange Wapper, een plaaggeest die dronken mensen angst aanjoeg " +
                                "in een 19de-eeuws gedicht. Ga zeker eens een kijkje nemen op het dakterras van het gebouw, je hebt er een panoramisch " +
                                "zicht over de stad Antwerpen.",
                        "Het Steen is een deel van een voormalige ringwalburg aan de rechter Schelde-oever in de stad Antwerpen. " +
                                "Het gebouw is sinds 2021 een toeristisch onthaalcentrum voor de stad Antwerpen",
                        "https://i.pinimg.com/originals/d7/86/45/d786459e4f60b06104ee618cfac0c7ed.jpg",
                        historischLocatie3, historischCentrumAntwerpen);
                Checkpoint historischCheckpoint4 = new Checkpoint("Het mas",
                        "Het MAS, of Museum aan de Stroom, is een museum in Antwerpen dat op 14 mei 2011 opende. Het MAS heeft acht " +
                                "tentoonstellingsruimtes en een collectie van circa 500.000 objecten. De focus van het MAS is de verbondenheid " +
                                "tussen Antwerpen en de wereld. De geschiedenis, kunst en cultuur van de havenstad Antwerpen, de internationale " +
                                "handel en scheepvaart en kunst en cultuur uit Europa, Afrika, Azië, Amerika en Oceanië staan centraal." +
                                "Het museum is gelegen in de oude haven op het Eilandje. Het gebouw is een ontwerp van Neutelings Riedijk Architecten. " +
                                "Het heeft een grondoppervlakte van 1.350 m² en een totale oppervlakte van 14.500 m² en is 60 meter hoog. " +
                                "Het dakterras met panoramazicht en de wandelboulevard die naar boven leidt zijn tot 's avonds laat gratis " +
                                "toegankelijk en vormen een toeristische trekpleister." +
                                "Vlak bij het MAS bevindt zich het tot Stadsarchief omgebouwde monumentale Stapelhuis Sint-Felix, " +
                                "het vroegere gebouw van het Loodswezen en het Red Star Line Museum.",
                        "Het MAS, of Museum aan de Stroom, is een museum in Antwerpen dat op 14 mei 2011 opende. Het MAS heeft acht tentoonstellingsruimtes " +
                                "en een collectie van circa 500.000 objecten.",
                        "https://mas.be/sites/mas/files/styles/teaser_2_3/public/rede_Antwerpen.jpg?itok=0MZIL9lI",
                        historischLocatie4, historischCentrumAntwerpen);
                allCheckpoints.add(historischCheckpoint1);
                allCheckpoints.add(historischCheckpoint2);
                allCheckpoints.add(historischCheckpoint3);
                allCheckpoints.add(historischCheckpoint4);

                Checkpoint meerjarenPlanCheckpoint1 = new Checkpoint("Wijzigingen opera plein",
                        "",
                        "",
                        "https://vlaamsbouwmeester.be/sites/default/files/styles/large/public/open_call_project_images_realization/MDC_TVBM_7-8-20_Operaplein_011_LR.jpg?itok=ihMyqlCF",
                        meerjarenPlanLocatie1,
                        meerjarenPlanOpera);
                Checkpoint meerjarenPlanCheckpoint2 = new AdvancedCheckpoint("Nieuwe fontein",
                        "",
                        "",
                        "",
                        meerjarenPlanLocatie2,
                        meerjarenPlanOpera,
                        "fontain", "gltf/fontain/scene.gltf", 0.08);
                Checkpoint meerjarenPlanCheckpoint3 = new AdvancedCheckpoint("Nieuwe bank",
                        "",
                        "",
                        "",
                        meerjarenPlanLocatie3,
                        meerjarenPlanOpera,
                        "bench", "gltf/modern_bench/scene.gltf", 10);
                allCheckpoints.add(meerjarenPlanCheckpoint1);
                allCheckpoints.add(meerjarenPlanCheckpoint2);
                allCheckpoints.add(meerjarenPlanCheckpoint3);


                Checkpoint gastronomischCheckpoint1 = new Checkpoint("Da Giovanni",
                        "Restaurant Da Giovanni ligt tegen de Onze-Lieve-Vrouwekathedraal, pal in het historische centrum van Antwerpen. " +
                                "Maar toch stapt u er binnen in een authentiek mediterraans interieur: rode, witte en groene tinten brengen de " +
                                "Italiaanse vlag tot leven. Italië op uw bord bij Da Giovanni in Antwerpen! Ook de kaart brengt Italië tot leven. " +
                                "Om van de zuiderse, temperamentvolle kelners nog maar te zwijgen. Zij serveren u dampende pasta’s en rijkelijk " +
                                "versierde pizza’s, gemaakt volgens de Italiaanse traditie. Bij Da Giovanni in Antwerpen proeft u artisanale gerechten, " +
                                "boordevol dagverse ingrediënten. Dat smaakt!",
                        "Restaurant Da Giovanni ligt tegen de Onze-Lieve-Vrouwekathedraal, pal in het historische centrum van Antwerpen. " +
                                "Maar toch stapt u er binnen in een authentiek mediterraans interieur: rode, witte en groene tinten brengen de " +
                                "Italiaanse vlag tot leven.",
                        "https://www.dagiovanni.be/wp-content/uploads/2018/05/Logo-dagiovanni-retina.png",
                        gastronomischLocatie1, gastronomischAntwerpen);
                Checkpoint gastronomischCheckpoint2 = new Checkpoint("Takumi Ramen & Yakisoba Kitchen",
                        "Voor meeste ramen en gerechten, realiseren ze een echte Japans smaak. Gyoza is gelijk wat je krijgt " +
                                "in Japan en heelrijk gebakken. Ze serveren ook zeer bijzondere japanse frisdrank genaamd Ramune. Deze hebben " +
                                "ze in 3 smaken: aardbei, ananas, meloen Ramen noodles heeft haar eerste restaurant geopend in 2007 in Düsseldorf, " +
                                "sindsdien is ramen noodles populair in verschillende landen",
                        "Voor meeste ramen en gerechten, realiseren ze een echte Japans smaak. Gyoza is gelijk wat " +
                                "je krijgt in Japan en heelrijk gebakken.",
                        "https://www.takumiramen.nl/wp-content/uploads/2018/05/Favicon.png",
                        gastronomischLocatie2, gastronomischAntwerpen);
                Checkpoint gastronomischCheckpoint3 = new Checkpoint("Bier Centraal",
                        "De passie voor bier uit zich in alle aspecten van de zaak: van de menukaart tot het interieur. " +
                                "De inrichting van Bier Central dompelt je meteen onder in een nostalgische sfeer; kies je voor een " +
                                "kruk aan de bar, of hou je het liever wat intiemer en neem je liever plaats in een bierton? Ook de menukaart " +
                                "is impressionant, het is een ware Bierencyclopedie van meer dan 120 pagina’s in boekvorm. Ieder detail " +
                                "straalt de goede, oude tijd uit. Heb je na een biertje, zin in een hapje? Geen probleem. Onze typisch " +
                                "Vlaamse gerechten worden gecombineerd met het perfect bijhorende bier. Onze biersommeliers zijn echte " +
                                "zythologen en gekend voor hun food pairing skills. Ben je zelf ook benieuwd naar bier? Onze kenners delen " +
                                "graag hun kennis tijdens bier tastings in groep of individueel.",
                        "Bier Central is ondertussen al even een gevestigde waarde op de de Keyserlei in Antwerpen. " +
                                "Voor het uitgebreide aanbod bier alleen al komen mensen uit alle uithoeken van de wereld, maar dit biercafé heeft " +
                                "zoveel meer te bieden!",
                        "https://files.resengo.com/companylogo/9df056eb-ea12-4527-b856-c9b8a4792f3b-logo.jpg",
                        gastronomischLocatie3, gastronomischAntwerpen);
                Checkpoint gastronomischCheckpoint4 = new Checkpoint("The Bistro",
                        "The Bistro is een sfeervol Brasserie-restaurant met een tijdloos decor, uniek gesitueerd in het hartje " +
                                "van de stad, op de verkeersvrije verbinding tussen de drukke Meir en de trendy Schuttershofstraat." +
                                "De aantrekkelijke kaart omvat zomaar eventjes 150 gerechten gaande van mooi gepresenteerde snacks tot " +
                                "feestelijke kreeftbereidingen. De infrastructuur van het restaurant is geschikt voor de ontvangst van groepen " +
                                "en/of reisgezelschappen tot ongeveer 80 personen.",
                        "The Bistro is een sfeervol Brasserie-restaurant met een tijdloos decor, uniek gesitueerd in het hartje van de stad.",
                        "https://www.thebistro.be/favicon/android-icon-192x192.png",
                        gastronomischLocatie4, gastronomischAntwerpen);
                allCheckpoints.add(gastronomischCheckpoint1);
                allCheckpoints.add(gastronomischCheckpoint2);
                allCheckpoints.add(gastronomischCheckpoint3);
                allCheckpoints.add(gastronomischCheckpoint4);

                /*AdvancedCheckpoint advuilbak = new AdvancedCheckpoint("vuilbak","des","shortdes","/",test3Dvuilbak,meerjarenPlanOpera,"trashcan","/",1);
                AdvancedCheckpoint adboom = new AdvancedCheckpoint("boom","des","shortdes","/",test3Dboom,meerjarenPlanOpera,"tree","/",2);
                AdvancedCheckpoint adfontain = new AdvancedCheckpoint("fontaine","des","shortdes","/",test3Dfontain,meerjarenPlanOpera,"fontain","/",1);
                AdvancedCheckpoint adbench = new AdvancedCheckpoint("bench","des","shortdes","/",test3Dbench,meerjarenPlanOpera,"bench","/",2);
                allCheckpoints.add(advuilbak);
                allCheckpoints.add(adboom);
                allCheckpoints.add(adfontain);
                allCheckpoints.add(adbench);*/

                for (Checkpoint c:allCheckpoints) {
                        checkpointRepository.save(c);
                }

                //PROJECT ADD CHECKPOINT
                historischCentrumAntwerpen.addCheckpoint(historischCheckpoint1);
                historischCentrumAntwerpen.addCheckpoint(historischCheckpoint2);
                historischCentrumAntwerpen.addCheckpoint(historischCheckpoint3);
                historischCentrumAntwerpen.addCheckpoint(historischCheckpoint4);
                projectRepository.saveAndFlush(historischCentrumAntwerpen);

                gastronomischAntwerpen.addCheckpoint(gastronomischCheckpoint1);
                gastronomischAntwerpen.addCheckpoint(gastronomischCheckpoint2);
                gastronomischAntwerpen.addCheckpoint(gastronomischCheckpoint3);
                gastronomischAntwerpen.addCheckpoint(gastronomischCheckpoint4);
                projectRepository.saveAndFlush(gastronomischAntwerpen);

                meerjarenPlanOpera.addCheckpoint(meerjarenPlanCheckpoint1);
                meerjarenPlanOpera.addCheckpoint(meerjarenPlanCheckpoint2);
                meerjarenPlanOpera.addCheckpoint(meerjarenPlanCheckpoint3);
                projectRepository.saveAndFlush(meerjarenPlanOpera);

                //TAG AANMAKEN EN SAVE
                Tag tagBezienswaardigheden = new Tag("bezienswaardigheden");
                Tag tagModern = new Tag("modern");
                Tag taghistorisch = new Tag("Historisch");
                Tag tagDrank = new Tag("Drank");
                Tag tagRestaurant = new Tag("Restaurant");
                alltags.add(tagBezienswaardigheden);
                alltags.add(tagModern);
                alltags.add(taghistorisch);
                alltags.add(tagDrank);
                alltags.add(tagRestaurant);

                for (Tag t:alltags) {
                        tagRepository.save(t);
                }

                //TAG ADD CHECKPOINT
                historischCheckpoint1.addTag(tagBezienswaardigheden);
                historischCheckpoint2.addTag(tagBezienswaardigheden);
                historischCheckpoint3.addTag(tagBezienswaardigheden);
                historischCheckpoint4.addTag(tagBezienswaardigheden);
                tagBezienswaardigheden.addCheckpoint(historischCheckpoint1);
                tagBezienswaardigheden.addCheckpoint(historischCheckpoint2);
                tagBezienswaardigheden.addCheckpoint(historischCheckpoint3);
                tagBezienswaardigheden.addCheckpoint(historischCheckpoint4);

                historischCheckpoint1.addTag(taghistorisch);
                historischCheckpoint2.addTag(taghistorisch);
                historischCheckpoint3.addTag(taghistorisch);
                taghistorisch.addCheckpoint(historischCheckpoint1);
                taghistorisch.addCheckpoint(historischCheckpoint2);
                taghistorisch.addCheckpoint(historischCheckpoint3);

                historischCheckpoint4.addTag(tagModern);
                tagModern.addCheckpoint(historischCheckpoint4);

                gastronomischCheckpoint1.addTag(tagRestaurant);
                gastronomischCheckpoint2.addTag(tagRestaurant);
                gastronomischCheckpoint4.addTag(tagRestaurant);
                tagRestaurant.addCheckpoint(gastronomischCheckpoint1);
                tagRestaurant.addCheckpoint(gastronomischCheckpoint2);
                tagRestaurant.addCheckpoint(gastronomischCheckpoint4);

                gastronomischCheckpoint3.addTag(tagDrank);
                gastronomischCheckpoint4.addTag(tagDrank);
                tagDrank.addCheckpoint(gastronomischCheckpoint3);
                tagDrank.addCheckpoint(gastronomischCheckpoint4);


                for (Checkpoint c:allCheckpoints) {
                        checkpointRepository.saveAndFlush(c);
                }
                for (Tag t:alltags) {
                        tagRepository.saveAndFlush(t);
                }


        }
}
