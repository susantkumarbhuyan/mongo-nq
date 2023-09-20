package io.mongonq.query;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.bson.BsonDocument;

/**
 * The ResultsIterator is the results from an operation, such as a query.
 *
 * @param <E> The type that this iterable will decode documents to.
 * @since 1.0
 */

public class ResultsIterator<E> implements Iterator<E>, Iterable<E>, Closeable {

	private Iterator<BsonDocument> results;
	private Class<E> clazz;

	public ResultsIterator(Iterator<BsonDocument> results, Class<E> clazz) {
		this.results = results;
		this.clazz = clazz;
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return results.hasNext();
	}

	@Override
	public E next() {
		if (!hasNext())
			throw new NoSuchElementException();

		BsonDocument document = results.next();
		return QueryBuilderUtil.mapToPojo(document, clazz);
	}

	@Override
	public void close() throws IOException {
		if (results instanceof Closeable) {
			Closeable closeable = (Closeable) results;
			closeable.close();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("remove() method is not supported");
	}

	boolean isCursor() {
		return true;
	}

	public int count() {
		if (iterator() instanceof Collection<?>) {
			return ((Collection<?>) iterator()).size();
		}
		return 0;
	}
}