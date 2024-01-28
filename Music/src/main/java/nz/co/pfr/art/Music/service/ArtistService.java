package nz.co.pfr.art.Music.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nz.co.pfr.art.Music.entities.ArtistRepository;

@Service
public class ArtistService {
    Logger log = LoggerFactory.getLogger(ArtistService.class);
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<String> getMostProductiveArtists(Integer topn) {

        var artists = artistRepository.findAll();

        var artistTrackCountMap = new HashMap<String, Integer>();
        
        for(int i = 0; i < artists.size(); i++) {
            var artist = artists.get(i);
            log.info("artist name {} cds {}", artist.getName(), artist.getCds().size());

            int trackCount = 0;
            for(int j = 0; j < artist.getCds().size(); j ++) {
                var cd = artist.getCds().get(j);
                log.info("cd title {} tracks {}", cd.getTitle(), cd.getTracks().size());
                trackCount+= cd.getTracks().size();

            }
            artistTrackCountMap.put(artist.getName(), trackCount);

        }
        List<String> mostProductiveArtists = new ArrayList();                 
        var entryList = new ArrayList<Entry<String, Integer>>(artistTrackCountMap.entrySet());
        entryList.sort((a1, a2) -> a2.getValue().compareTo(a1.getValue()));
        
        Iterator<Entry<String, Integer>> iter = entryList.iterator();
        
        int icnt = 1;
        while(iter.hasNext() && icnt <= topn ) {
        	icnt++;
        	Entry<String, Integer> entry = iter.next();
        	mostProductiveArtists.add(entry.getKey());
        }

        log.info("most productive artist list is: {}", mostProductiveArtists);
        return mostProductiveArtists;
    }
    
    public List<String> getMostProductiveArtistsSQL(Integer topn) {
    	return artistRepository.findTopList(topn);
    }


}
