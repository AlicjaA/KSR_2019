package extraction.Features;

import dataModel.Article;
import extraction.importanceMeasurment.LocalImportanceMeasures;

import java.util.ArrayList;


public enum QNFeatures {

    AVG_QI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.quantitativeImportance(article.getWords(), key);
            }
            return (sum/keys.size());
        }

    },
    AVG_SUM_BI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.binaryImportance(article.getWords(), key);
            }
            return (sum/keys.size());
        }

    },
    SUM_PI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.probabilisticImportance(article.getWords(), article.getTerms(), key);
            }
            return sum;
        }
    },
    SUM_PSI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.probabilisticSimilarityImportance(article.getWords(), article.getTerms(), key);
            }
            return sum;
        }
    },
    SUM_TF{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            double sum = 0.0;
            for (String key : keys) {
                sum += localImp.termFrequency(article.getWords(), key);
            }
            return sum;
        }
    },
    SUM_QI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.quantitativeImportance(article.getWords(), key);
            }
            return sum;
        }

    },
    AVG_PI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.probabilisticImportance(article.getWords(), article.getTerms(), key);;
            }
            return sum/keys.size();
        }

    },
    AVG_PSI{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.probabilisticSimilarityImportance(article.getWords(), article.getTerms(), key);
            }
            return sum/keys.size();
        }

    },
    AVG_DIST_KEY_TXT_BEGIN{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            Integer count = 0;
            for(String key: keys){
                if(localImp.binaryImportance(article.getWords(),key)>0){
                    sum +=article.getWords().indexOf(key);
                    count++;
                }
            }
            return sum/count;

        }

    },
    AVG_TF{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            for (String key : keys) {
                sum += localImp.termFrequency(article.getWords(), key);
            }
            return sum/keys.size();
        }

    },
    SUM_QI_F20P{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            int first20Percent = (int) Math.round(article.getWords().size()*0.2);
            ArrayList<String> first20PercentWords = new ArrayList<>();

            for(int i=0;i<first20Percent;++i){
                first20PercentWords.add(article.getWords().get(i));
            }

            for (String key : keys) {
                sum += localImp.quantitativeImportance(first20PercentWords, key);
            }
            return sum;
        }

    },
    SUM_BI_F10P_KEYS{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double sum = 0.0;
            int first10Percent;
            if(keys.size()>2){
            first10Percent = (int) Math.round(keys.size()*0.1);
            }
            else{first10Percent=1;}
            ArrayList<String> first10PercentKeys = new ArrayList<>();

            for(int i=0;i<first10Percent;++i){
                first10PercentKeys.add(keys.get(i));
            }
            for (String key : first10PercentKeys) {
                sum += localImp.binaryImportance(article.getWords(), key);
            }
            return sum;
        }

    },
    PSI_STRING_FEATURE{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double psi = Double.valueOf(localImp.probabilisticSimilarityImportance(article.getWords(), keys, article.getFeatureString()));
            return psi;
        }
    },
    TF_STRING_FEATURE{
        @Override
        public Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp){
            Double is = Double.valueOf(localImp.termFrequency(article.getWords(), article.getFeatureString()));
            return is;
        }
    };

    public abstract Double calculate(Article article, ArrayList<String> keys, LocalImportanceMeasures localImp);
}


