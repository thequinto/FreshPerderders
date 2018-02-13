package co.ga.freshpotatoes.domain.data;

import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import co.ga.freshpotatoes.domain.entity.Film;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedResponse extends Response {
    private Set<Film> recommendations;
    private Meta meta;

    public RecommendedResponse() {}
    
    public RecommendedResponse(Set<Film> recommendations, String metaLimit, String metaOffset) {
        this.recommendations = recommendations;
        this.meta = new Meta(metaLimit, metaOffset);
    }
    
    public Set<Film> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Set<Film> recommendations) {
        this.recommendations = recommendations;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{recommendations:[");
        Iterator<Film> i = recommendations.iterator();
        while (i.hasNext()) {
            s.append(i.next());
            if (i.hasNext())
                s.append(",\n\t");
        }
        s.append("],\nmeta:").append(meta)
        .append("}");
        return s.toString();
    }
    
    private class Meta {
        private int limit;
        private int offset;
        
        public Meta(String limit, String offset) {
            this.limit = Integer.parseInt(limit);
            this.offset = Integer.parseInt(offset);
        }
        
        public int getLimit() {
            return limit;
        }
        public void setLimit(int limit) {
            this.limit = limit;
        }
        public int getOffset() {
            return offset;
        }
        public void setOffset(int offset) {
            this.offset = offset;
        }
        public String toString() {
            return "{limit:" + limit + ",offset:" + offset + "}";
        }
    }
}
