package com.generic.dbcache.exception;

@SuppressWarnings("serial")
public class CacheException extends Exception {

	public enum CacheOperations {
		INSERT("While performing insertion into cache"), UPDATE("While performing updation within cache"),
		DELETE("While performing deletion from cache"), RETRIEVE("While performing retrieval from cache"),
		EXISTS("While performing exists from cache");

		private final String messageFromOperation;

		CacheOperations(String messageFromOperation) {
			this.messageFromOperation = messageFromOperation;
		}

		public String getMessageFromOperation() {
			return messageFromOperation;
		}
	}

	public enum Database {
		MONGODB("Problem occured while working with MongoDB."),
		CASSANDRA("Problem occured while working with Cassandra."),
		DYNAMODB("Problem occurred while working with DynamoDB.");

		private final String messageFromCurrentDatabase;

		Database(String messageFromCurrentDatabase) {
			this.messageFromCurrentDatabase = messageFromCurrentDatabase;
		}

		public String getMessageFromCurrentDatabase() {
			return messageFromCurrentDatabase;
		}
	}

	private final String message;
	private final Throwable throwable;
	private final CacheOperations cacheOperations;
	private final Database database;
	private final String key;
	private final String collectionValues;

	@SuppressWarnings("unused")
	public static class Builder {
		private Throwable throwable;
		private CacheOperations whilePerforming;
		private Database inDatabase;
		private String forKey;
		private String withCollectionValues;

		public Builder(Exception e) {
			throwable = e;
		}

		public String getWithCollectionValues() {
			return withCollectionValues;
		}

		public Builder withCollectionValues(String withCollectionValues) {
			this.withCollectionValues = withCollectionValues;
			return this;
		}

		public String getForKey() {
			return forKey;
		}

		public Builder forKey(String forKey) {
			this.forKey = forKey;
			return this;
		}

		public CacheOperations getWhilePerforming() {
			return whilePerforming;
		}

		public Builder whilePerforming(CacheOperations whilePerforming) {
			this.whilePerforming = whilePerforming;
			return this;
		}

		public Database getInDatabase() {
			return inDatabase;
		}

		public Builder inDatabase(Database inDatabase) {
			this.inDatabase = inDatabase;
			return this;
		}

		public CacheException build() {
			return new CacheException(this);
		}
	}

	public CacheException(Builder builder) {
		this.throwable = builder.throwable;
		this.cacheOperations = builder.whilePerforming;
		this.database = builder.inDatabase;
		this.key = builder.forKey;
		this.collectionValues = builder.withCollectionValues;
		StringBuilder cacheExceptionMessage = new StringBuilder();
		cacheExceptionMessage.append(database.getMessageFromCurrentDatabase());
		cacheExceptionMessage.append(cacheOperations.getMessageFromOperation());
		cacheExceptionMessage.append(key);
		cacheExceptionMessage.append(collectionValues);
		cacheExceptionMessage.append(throwable.getMessage());
		message = cacheExceptionMessage.toString();
	}

	public String getMessage() {
		return message;
	}
}
