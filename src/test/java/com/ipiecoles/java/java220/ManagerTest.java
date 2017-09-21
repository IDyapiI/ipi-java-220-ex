package com.ipiecoles.java.java220;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.TestUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class ManagerTest {

    @Test
    public void exo400Heritage() throws IllegalAccessException {
        //Comme pour la classe Commercial, faire hériter la classe Manager d'Employe et implémenter
        //la méthode abstraite getPrimeAnnuelle pour qu'elle retourne quelque chose et que
        //la compilation passe (la méthode sera implémentée plus tard)
        Assertions.assertThat(Manager.class.getSuperclass()).isEqualTo(Employe.class);
        TestUtils.checkNotAbstractClass(Manager.class);
    }

    @Test
    public void exo401Equipe() throws Exception {
        //Modifier la classe Manager pour ajouter un attribut equipe permettant de stocker un ensemble non ordonné de techniciens
        //avec son getter et son setter

        TestUtils.checkPrivateField(Manager.class, "equipe", HashSet.class);
        TestUtils.checkMethod(Manager.class, "getEquipe", HashSet.class);
        TestUtils.checkMethod(Manager.class, "setEquipe", void.class, HashSet.class);
        Manager d = Manager.class.newInstance();
        Assertions.assertThat(TestUtils.invokeGetter(d, "equipe")).isInstanceOf(HashSet.class);
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).isNotNull();
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).hasSize(0);

        HashSet<Technicien> techniciens = new HashSet<>();
        techniciens.add(new Technicien());

        TestUtils.invokeSetter(d, "equipe", techniciens);
        Assertions.assertThat(TestUtils.invokeGetter(d, "equipe")).isEqualTo(techniciens);
    }

    @Test
    public void exo402AjoutMembreEquipe() throws Exception {
        //Ajouter une méthode ajoutTechnicienEquipe qui prend un paramètre un technicien et qui
        //l'ajoute dans l'équipe

        Manager d = Manager.class.newInstance();

        Technicien t = new Technicien();

        TestUtils.callMethod(d, "ajoutTechnicienEquipe", t);
        Assertions.assertThat(TestUtils.invokeGetter(d, "equipe")).isInstanceOf(HashSet.class);
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).isNotNull();
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).hasSize(1);
        Assertions.assertThat(((HashSet) TestUtils.invokeGetter(d, "equipe")).iterator().next()).isEqualTo(t);
    }

    @Test
    public void exo403SetSalaire() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //Surcharger le setter de l'attribut salaire pour qu'il renvoie la valeur du salaire multipliée par l'index manager,
        //auquel on ajoute 10% (sur le salaire passé en paramètre) par membre d'équipe

        Manager d = Manager.class.getConstructor().newInstance();
        try {
            TestUtils.invokeSetter(d, "salaire", 1000.0);
            Assertions.assertThat(TestUtils.invokeGetter(d, "salaire")).isEqualTo(1300.0);
        }
        catch(Exception exception){
            Assertions.fail("L'affectation n'aurait pas du lancer une exception");
        }

        try {
            TestUtils.invokeSetter(d, "equipe", Stream.of(new Technicien()).collect(Collectors.toSet()));
            TestUtils.invokeSetter(d, "salaire", 1000.0);
            Assertions.assertThat(TestUtils.invokeGetter(d, "salaire")).isEqualTo(1400.0);
        }
        catch(Exception exception){
            Assertions.fail("L'affectation n'aurait pas du lancer une exception");
        }
    }

    @Test
    public void exo404GetPrimeAnnuelle() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //Modifier le code de la méthode getPrimeAnnuelle pour qu'elle renvoie la prime de base, à laquelle on ajoute
        //la prime du manager en fonction du nombre de membres
        //de son équipe (en utilisant Entreprise.PRIME_MANAGER_PAR_TECHNICIEN)
        Manager d = Manager.class.getConstructor().newInstance();
        try {
            Assertions.assertThat(TestUtils.callMethod(d, "getPrimeAnnuelle")).isEqualTo(1008.5);
        }
        catch(Exception exception){
            Assertions.fail("L'affectation n'aurait pas du lancer une exception");
        }

        try {
            TestUtils.invokeSetter(d, "equipe", Stream.of(new Technicien()).collect(Collectors.toSet()));
            Assertions.assertThat(TestUtils.callMethod(d, "getPrimeAnnuelle")).isEqualTo(1258.5);
        }
        catch(Exception technicienException){
            Assertions.fail("L'affectation n'aurait pas du lancer une exception");
        }

        try {
            TestUtils.invokeSetter(d, "equipe", Stream.of(new Technicien(), new Technicien()).collect(Collectors.toSet()));
            Assertions.assertThat(TestUtils.callMethod(d, "getPrimeAnnuelle")).isEqualTo(1508.5);
        }
        catch(Exception technicienException){
            Assertions.fail("L'affectation n'aurait pas du lancer une exception");
        }

    }

    @Test
    public void exo405AugmenterSalaireEquipe() throws Exception {
        //Ajoutée une méthode interne augmenterSalaireEquipe qui augmente le salaire de tous les membres de l'équipe
        //d'un manager par un pourcentage (Double) avec la méthode précédemment définie dans Employe
        //Voir ensuite les deux dernières lignes du test et essayer de comprendre pourquoi

        TestUtils.checkPrivateMethod(Manager.class, "augmenterSalaireEquipe", void.class, double.class);

        Manager d = Manager.class.getConstructor().newInstance();
        TestUtils.invokeSetter(d, "equipe", Stream.of(new Technicien(), new Technicien()).collect(Collectors.toSet()));
        TestUtils.callMethodPrimitiveParameters(d, "augmenterSalaireEquipe", 0.05d);
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).isNotNull();
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).hasSize(2);
        Iterator iterator = ((HashSet) TestUtils.invokeGetter(d, "equipe")).iterator();
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(0.0);
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(0.0);

        d = Manager.class.getConstructor().newInstance();
        TestUtils.invokeSetter(d, "equipe", Stream.of(Technicien.class.getConstructor().newInstance(null, null, null, null, 1000.0, 1), Technicien.class.getConstructor().newInstance(null, null, null, null, 1000.0, 1)).collect(Collectors.toSet()));
        TestUtils.callMethodPrimitiveParameters(d, "augmenterSalaireEquipe", 0.50d);
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).isNotNull();
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).hasSize(2);
        iterator = ((HashSet) TestUtils.invokeGetter(d, "equipe")).iterator();
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(1500.0);
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(1500.0);
    }

    @Test
    public void exo406AugmenterSalaire() throws Exception {
        //Surcharger la méthode augmenterSalaire pour systématiquement augmenter le salaire de l'équipe
        //d'un manager avant d'augmenter le salaire du manager...

        TestUtils.checkMethod(Manager.class, "augmenterSalaire", void.class, double.class);

        Manager d = Manager.class.getConstructor().newInstance();
        TestUtils.invokeSetter(d, "salaire", 1000.0);

        TestUtils.invokeSetter(d, "equipe", Stream.of(new Technicien(), new Technicien()).collect(Collectors.toSet()));
        TestUtils.callMethodPrimitiveParameters(d, "augmenterSalaire", (double)0.05d);
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).isNotNull();
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).hasSize(2);
        Iterator iterator = ((HashSet) TestUtils.invokeGetter(d, "equipe")).iterator();
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(0.0);
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(0.0);
        Assertions.assertThat(TestUtils.invokeGetter(d, "salaire")).isEqualTo(1365.0);


        d = Manager.class.getConstructor().newInstance();
        TestUtils.invokeSetter(d, "salaire", 1000.0);
        TestUtils.invokeSetter(d, "equipe", Stream.of(Technicien.class.getConstructor().newInstance(null, null, null, null, 1000.0, 1), Technicien.class.getConstructor().newInstance(null, null, null, null, 1000.0, 1)).collect(Collectors.toSet()));
        TestUtils.callMethodPrimitiveParameters(d, "augmenterSalaire", 0.50d);
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).isNotNull();
        Assertions.assertThat((HashSet)TestUtils.invokeGetter(d, "equipe")).hasSize(2);
        iterator = ((HashSet) TestUtils.invokeGetter(d, "equipe")).iterator();
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(1500.0);
        Assertions.assertThat(TestUtils.invokeGetter(iterator.next(), "salaire")).isEqualTo(1500.0);
        Assertions.assertThat(TestUtils.invokeGetter(d, "salaire")).isEqualTo(1950.0);
    }
}
