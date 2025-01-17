package comptoirs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import comptoirs.entity.Commande;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {

    /**
     * Calcule le montant des articles commandés dans une commande
     * @param numeroCommande le numéro de la commande à traiter
     * @return le montant des articles commandés, en tenant compte de la remise
     */
    @Query(
            value = """
        SELECT SUM(quantite*prix_unitaire * (1-remise) ) as montant
        FROM Commande
        INNER JOIN Ligne ON COMMANDE_NUMERO = NUMERO
        INNER JOIN Produit ON PRODUIT_REFERENCE= REFERENCE
        WHERE Commande.numero= :numeroCommande
        """, nativeQuery = true
    )
    BigDecimal montantArticles(Integer numeroCommande);

    /**
     * Calcule le montant des articles commandés dans les commandes d'un même client
     * @param codeClient code du client
     * @return les numéros, les ports et le montant total après remise de chacune des commandes du client
     */
    @Query(
            value = """
            SELECT commande.numero as numero, commande.port as port,SUM(quantite*prix_unitaire * (1-remise) ) as montant
            FROM Commande
            INNER JOIN Ligne ON COMMANDE_NUMERO = NUMERO
            INNER JOIN Produit ON PRODUIT_REFERENCE= REFERENCE
            WHERE Commande.client_code= :codeClient            
            GROUP BY COMMANDE.NUMERO
        """, nativeQuery = true
    )
    List<MontantParClientParCommande> montantArticlesParClient(String codeClient);



}
