package io.mongonq;

import java.util.concurrent.TimeUnit;

import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.client.model.Collation;

public class AggregationOptions {

	private final Integer batchSize;
	private final Boolean allowDiskUse;
	private final long maxTimeMS;
	private final Boolean bypassDocumentValidation;
	private final Collation collation;

	AggregationOptions(final Builder builder) {
		batchSize = builder.batchSize;
		allowDiskUse = builder.allowDiskUse;
		maxTimeMS = builder.maxTimeMS;
		bypassDocumentValidation = builder.bypassDocumentValidation;
		collation = builder.collation;
	}

	/**
	 * If true, this enables external sort capabilities, otherwise $sort produces an
	 * error if the operation consumes 10 percent or more of RAM.
	 *
	 * @return true if aggregation stages can write data to temporary files
	 * @mongodb.server.release 2.6
	 */
	public Boolean getAllowDiskUse() {
		return allowDiskUse;
	}

	/**
	 * The size of batches to use when iterating over results.
	 *
	 * @return the batch size
	 * @mongodb.server.release 2.6
	 */
	public Integer getBatchSize() {
		return batchSize;
	}

	/**
	 * Gets the maximum execution time for the aggregation command.
	 *
	 * @param timeUnit the time unit for the result
	 * @return the max time
	 * @mongodb.server.release 2.6
	 * @since 2.12
	 */
	public long getMaxTime(final TimeUnit timeUnit) {
		return timeUnit.convert(maxTimeMS, TimeUnit.MILLISECONDS);
	}

	/**
	 * Gets whether to bypass document validation, or null if unspecified. The
	 * default is null.
	 *
	 * @return whether to bypass document validation, or null if unspecified.
	 * @since 2.14
	 * @mongodb.server.release 3.2
	 */
	public Boolean getBypassDocumentValidation() {
		return bypassDocumentValidation;
	}

	/**
	 * Returns the collation options
	 *
	 * @return the collation options
	 * @since 3.4
	 * @mongodb.server.release 3.4
	 */
	public Collation getCollation() {
		return collation;
	}

	@Override
	public String toString() {
		return "AggregationOptions{" + "batchSize=" + batchSize + ", allowDiskUse=" + allowDiskUse + ", maxTimeMS="
				+ maxTimeMS + ", bypassDocumentValidation=" + bypassDocumentValidation + ", collation=" + collation
				+ "}";
	}

	/**
	 * Creates a new Builder for {@code AggregationOptions}.
	 *
	 * @return a new empty builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for creating {@code AggregationOptions}.
	 *
	 * @mongodb.server.release 2.2
	 * @mongodb.driver.manual reference/command/aggregate/ aggregate
	 */
	@NotThreadSafe
	public static class Builder {
		private Integer batchSize;
		private Boolean allowDiskUse;
		private long maxTimeMS;
		private Boolean bypassDocumentValidation;
		private Collation collation;

		private Builder() {
		}

		/**
		 * Sets the size of batches to use when iterating over results. Can be null.
		 *
		 * @param size the batch size to apply to the cursor
		 * @return {@code this} so calls can be chained
		 * @mongodb.server.release 2.6
		 */
		public Builder batchSize(final Integer size) {
			batchSize = size;
			return this;
		}

		/**
		 * Set whether to enable external sort capabilities. If set to false, $sort
		 * produces an error if the operation consumes 10 percent or more RAM.
		 *
		 * @param allowDiskUse whether or not aggregation stages can write data to
		 *                     temporary files
		 * @return {@code this} so calls can be chained
		 * @mongodb.server.release 2.6
		 */
		public Builder allowDiskUse(final Boolean allowDiskUse) {
			this.allowDiskUse = allowDiskUse;
			return this;
		}

		/**
		 * Sets the maximum execution time for the aggregation command.
		 *
		 * @param maxTime  the max time
		 * @param timeUnit the time unit
		 * @return {@code this} so calls can be chained
		 * @mongodb.server.release 2.6
		 */
		public Builder maxTime(final long maxTime, final TimeUnit timeUnit) {
			maxTimeMS = timeUnit.convert(maxTime, TimeUnit.MILLISECONDS);
			return this;
		}

		/**
		 * Sets whether to bypass document validation.
		 *
		 * @param bypassDocumentValidation whether to bypass document validation, or
		 *                                 null if unspecified
		 * @return this
		 * @since 2.14
		 * @mongodb.server.release 3.2
		 */
		public Builder bypassDocumentValidation(final Boolean bypassDocumentValidation) {
			this.bypassDocumentValidation = bypassDocumentValidation;
			return this;
		}

		/**
		 * Sets the collation
		 *
		 * @param collation the collation
		 * @return this
		 * @since 3.4
		 * @mongodb.server.release 3.4
		 */
		public Builder collation(final Collation collation) {
			this.collation = collation;
			return this;
		}

		/**
		 * Return the options based on this builder.
		 *
		 * @return the aggregation options
		 */
		public AggregationOptions build() {
			return new AggregationOptions(this);
		}
	}

}
