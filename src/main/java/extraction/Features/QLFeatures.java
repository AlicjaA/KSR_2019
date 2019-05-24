package extraction.Features;

import dataModel.Article;

public enum QLFeatures {
        STRING{
            @Override
            public String calculate(Article article){
                return article.getFeatureString();
            }
        };

        public abstract String calculate(Article article);
}
