package nz.co.pfr.art.Music.entities;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
	@Query(value = "SELECT E.NAME\r\n"
			+ "FROM (\r\n"
			+ "    SELECT B.ARTISTID,  SUM(A.TRACK_CNT) TOT_TRACT_CNT\r\n"
			+ "    FROM (\r\n"
			+ "         SELECT CDID, COUNT(TRACKID) as TRACK_CNT\r\n"
			+ "         FROM TRACK\r\n"
			+ "         GROUP BY CDID\r\n"
			+ "    ) A INNER JOIN CD AS B ON B.CDID = A.CDID\r\n"
			+ "    GROUP BY B.ARTISTID\r\n"
			+ ") D \r\n"
			+ "INNER JOIN ARTIST E ON E.ARTISTID = D.ARTISTID\r\n"
			+ "ORDER BY TOT_TRACT_CNT DESC LIMIT :topn", nativeQuery = true)
	List<String> findTopList(@Param("topn") Integer topn); 
}
