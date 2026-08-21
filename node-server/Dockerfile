# 
FROM node:18-alpine

# 
WORKDIR /app

# 
COPY node-server/package*.json ./

# 
RUN npm install

# 
COPY node-server/. .

#
COPY src/main/resources/static ./public

# 
EXPOSE 3000

# 
CMD ["node", "server.js"]