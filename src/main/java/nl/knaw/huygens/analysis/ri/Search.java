/**
   Copyright (c) 2007, University of Pittsburgh

   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:

   * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

   * Redistributions in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided
   with the distribution.

   * Neither the name of the University of Pittsburgh nor the names
   of its contributors may be used to endorse or promote products
   derived from this software without specific prior written
   permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/

package nl.knaw.huygens.analysis.ri;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import pitt.search.semanticvectors.CloseableVectorStore;
import pitt.search.semanticvectors.Flags;
import pitt.search.semanticvectors.LuceneUtils;
import pitt.search.semanticvectors.SearchResult;
import pitt.search.semanticvectors.VectorSearcher;
import pitt.search.semanticvectors.VectorStoreReader;
import pitt.search.semanticvectors.ZeroVectorException;

/**
 * The Search class parses command line arguments, with a tiny bit of extra logic for vector stores.
 */

public class Search {
  private static final Logger logger = Logger.getLogger(Search.class.getCanonicalName());

  private CloseableVectorStore queryVecReader;
  private CloseableVectorStore searchVecReader;
  private LuceneUtils luceneUtils;

  public Search(String[] args) {
    /**
     * The constructor parses command line arguments, with a tiny bit of 
     * extra logic for vector stores. The it opens the corresponding vector 
     * and lucene indexes.
     */

    // Stage i. Assemble command line options.
    args = Flags.parseCommandLineFlags(args);

    // If Flags.searchvectorfile wasn't set, it defaults to Flags.queryvectorfile.
    if (Flags.searchvectorfile.equals("")) {
      Flags.searchvectorfile = Flags.queryvectorfile;
    }

    // Stage ii. Open vector stores, and Lucene utils.
    try {
      // Default VectorStore implementation is (Lucene) VectorStoreReader.
      logger.info("Opening query vector store from file: " + Flags.queryvectorfile);
      queryVecReader = VectorStoreReader.openVectorStore(Flags.queryvectorfile);

      // Open second vector store if search vectors are different from query vectors.
      if (Flags.queryvectorfile.equals(Flags.searchvectorfile)) {
        searchVecReader = queryVecReader;
      } else {
        logger.info("Opening search vector store from file: " + Flags.searchvectorfile);
        searchVecReader = VectorStoreReader.openVectorStore(Flags.searchvectorfile);
      }

      if (Flags.luceneindexpath != "") {
        try {
          luceneUtils = new LuceneUtils(Flags.luceneindexpath);
        } catch (IOException e) {
          logger.info("Couldn't open Lucene index at " + Flags.luceneindexpath);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // This takes the slice of args from argc to end.
    if (!Flags.matchcase) {
      for (int i = 0; i < args.length; ++i) {
        args[i] = args[i].toLowerCase();
      }
    }

  }

  /**
   * Takes a user's query, creates a query vector, and searches a vector store.
   * @param new String query  ;
   * @param numResults Number of search results to be returned in a ranked list.
   * @return Linked list containing <code>numResults</code> search results.
   */
  public LinkedList<SearchResult> RunSearch(int numResults, String query) throws IllegalArgumentException {

    VectorSearcher vecSearcher;
    LinkedList<pitt.search.semanticvectors.SearchResult> results = new LinkedList<SearchResult>();
    // Stage iii. Perform search according to which searchType was selected.
    // Most options have corresponding dedicated VectorSearcher subclasses.
    try {
      vecSearcher = new VectorSearcher.VectorSearcherCosine(queryVecReader, searchVecReader, luceneUtils, new String[] { query });
      logger.info("Searching term vectors, searchtype SUM ... \n");
      results = vecSearcher.getNearestNeighbors(numResults);
    } catch (ZeroVectorException zve) {
      logger.info(zve.getMessage());
      results = new LinkedList<SearchResult>();
    }
    return results;
  }

  public void close() {
    queryVecReader.close();
    if (!Flags.queryvectorfile.equals(Flags.searchvectorfile)) {
      searchVecReader.close();
    }
  }
}
